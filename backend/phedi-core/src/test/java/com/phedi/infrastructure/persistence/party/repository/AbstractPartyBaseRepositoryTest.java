package com.phedi.infrastructure.persistence.party.repository;

import static com.phedi.support.PartyTestFixtures.organization;
import static com.phedi.support.PartyTestFixtures.organizationDomain;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.util.ReflectionTestUtils;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.PartyBaseJpaRepository;
import com.phedi.support.PartyTestFixtures;

@ExtendWith(MockitoExtension.class)
class AbstractPartyBaseRepositoryTest {

    @Mock
    private PartyBaseJpaRepository<OrganizationEntity> jpaRepository;

    @Mock
    private PartyPersistenceMapper<Organization, OrganizationEntity> mapper;

    @Mock
    private AuditorAware<String> auditorAware;

    private OrganizationRepositoryImpl repository;

    private static final String PUBLIC_ID = PartyTestFixtures.identifier().value();

    @BeforeEach
    void setUp() {
        repository = new OrganizationRepositoryImpl(jpaRepository, mapper);

        ReflectionTestUtils.setField(repository, "auditorAware", auditorAware);
    }

    @Test
    void shouldRejectUpdateWithoutIdentifier() {
        Organization domain = organizationDomain();
        domain.setIdentifier(null);

        assertThatThrownBy(() -> repository.update(domain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Identifier is required for update");

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingUnknownOrganization() {
        Organization domain = organizationDomain();
        domain.setIdentifier(new Identifier(PUBLIC_ID));

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(domain))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Organization not found: " + PUBLIC_ID);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldUpdateLoadedEntityAndSave() {
        Organization domain = organizationDomain();
        domain.setIdentifier(new Identifier(PUBLIC_ID));
        OrganizationEntity existing = organization();

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.of(existing));
        when(jpaRepository.save(existing)).thenReturn(existing);

        when(mapper.toDomain(existing)).thenReturn(domain);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.update(domain);

        verify(mapper).updateEntity(domain, existing);
        assertThat(existing.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(existing);
    }

    @Test
    void shouldSoftDeleteOnDelete() {
        OrganizationEntity entity = organization();

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.deleteByIdentifier(new Identifier(PUBLIC_ID));

        assertThat(entity.getIsDeleted()).isTrue();
        assertThat(entity.getDeletedAt()).isNotNull();
        assertThat(entity.getDeletedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenDeletingUnknown() {
        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.deleteByIdentifier(new Identifier(PUBLIC_ID));

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldActivate() {
        OrganizationEntity entity = organization();
        entity.setIsActive(false);

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.activate(new Identifier(PUBLIC_ID));

        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenActivatingUnknown() {
        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.activate(new Identifier(PUBLIC_ID));

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldDeactivate() {
        OrganizationEntity entity = organization();

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.deactivate(new Identifier(PUBLIC_ID));

        assertThat(entity.getIsActive()).isFalse();
        assertThat(entity.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenDeactivatingUnknown() {
        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.deactivate(new Identifier(PUBLIC_ID));

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldUseSystemAsFallbackAuditor() {
        OrganizationEntity entity = organization();

        when(jpaRepository.findByPublicIdAndIsDeletedFalse(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty());

        repository.activate(new Identifier(PUBLIC_ID));

        assertThat(entity.getUpdatedBy()).isEqualTo("SYSTEM");
    }
}
