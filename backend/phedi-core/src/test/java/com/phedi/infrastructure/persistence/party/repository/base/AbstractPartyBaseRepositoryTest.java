package com.phedi.infrastructure.persistence.party.repository.base;

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
import com.phedi.infrastructure.persistence.exception.ResourceNotFoundException;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.OrganizationRepositoryImpl;
import com.phedi.support.PartyTestFixtures;

@ExtendWith(MockitoExtension.class)
class AbstractPartyBaseRepositoryTest {

    @Mock
    private PartyBaseJpaRepository<OrganizationEntity> jpaRepository;

    @Mock
    private PartyPersistenceMapper<Organization, OrganizationEntity> mapper;

    @Mock
    private AuditorAware<String> auditorAware;

    private AbstractPartyBaseRepository<Organization, OrganizationEntity> repository;

    private static Identifier PUBLIC_ID = PartyTestFixtures.identifier();

    @BeforeEach
    void setUp() {
        repository = new OrganizationRepositoryImpl(jpaRepository, mapper);

        ReflectionTestUtils.setField(repository, "auditorAware", auditorAware);
    }

    @Test
    void shouldRejectUpdateWithoutIdentifier() {
        Organization domain = organizationDomain();
        domain.setIdentifier(null);

        assertThatThrownBy(() -> repository.checkIdentifierExist(domain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Identifier is required for update");
    }

    @Test
    void shouldThrowWhenUpdatingUnknownOrganization() {
        Organization domain = organizationDomain();
        domain.setIdentifier(PUBLIC_ID);

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(domain))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Organization not found: " + PUBLIC_ID.value());

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldUpdateLoadedEntityAndSave() {
        Organization domain = organizationDomain();
        domain.setIdentifier(PUBLIC_ID);
        OrganizationEntity existing = organization();

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(existing));

        when(jpaRepository.save(existing)).thenReturn(existing);

        when(mapper.toDomain(existing)).thenReturn(domain);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.update(domain);

        verify(mapper).updateEntity(domain, existing);
        assertThat(existing.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(existing);
    }

    @Test
    void shouldReturnOptionalEmptyWhenNotFoundEntityNoThrowsException() {
        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());
        assertThat(repository.findByIdentifier(PUBLIC_ID)).isEmpty();
    }

    @Test
    void shouldSoftDeleteOnDelete() {
        OrganizationEntity entity = organization();

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.deleteByIdentifier(PUBLIC_ID);

        assertThat(entity.getIsDeleted()).isTrue();
        assertThat(entity.getDeletedAt()).isNotNull();
        assertThat(entity.getDeletedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenDeletingUnknown() {
        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.deleteByIdentifier(PUBLIC_ID);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldActivate() {
        OrganizationEntity entity = organization();
        entity.setIsActive(false);

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.activate(PUBLIC_ID);

        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenActivatingUnknown() {
        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.activate(PUBLIC_ID);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldDeactivate() {
        OrganizationEntity entity = organization();

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        repository.deactivate(PUBLIC_ID);

        assertThat(entity.getIsActive()).isFalse();
        assertThat(entity.getUpdatedBy()).isEqualTo("alice");
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldNotSaveWhenDeactivatingUnknown() {
        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());

        repository.deactivate(PUBLIC_ID);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldUseSystemAsFallbackAuditor() {
        OrganizationEntity entity = organization();

        when(repository.getEntityByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty());

        repository.activate(PUBLIC_ID);

        assertThat(entity.getUpdatedBy()).isEqualTo("SYSTEM");
    }
}
