package com.phedi.infrastructure.persistence.party.repository.base;

import static com.phedi.support.PartyTestFixtures.organization;
import static com.phedi.support.PartyTestFixtures.organizationDomain;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.OrganizationRepositoryImpl;
import com.phedi.support.PartyTestFixtures;

@ExtendWith(MockitoExtension.class)
class AbstractPartyRepositoryTest {

    @Mock
    private PartyBaseJpaRepository<OrganizationEntity> jpaRepository;

    @Mock
    private PartyPersistenceMapper<Organization, OrganizationEntity> mapper;

    @Mock
    private PublicIdGenerator publicIdGenerator;

    private AbstractPartyRepository<Organization, OrganizationEntity> repository;

    @BeforeEach
    void setUp() {
        repository = new OrganizationRepositoryImpl(jpaRepository, mapper);

        ReflectionTestUtils.setField(repository, "publicIdGenerator", publicIdGenerator);
    }

    @Test
    void shouldGenerateIdentifierWhenDomainHasNone() {
        Organization domain = organizationDomain();
        OrganizationEntity entity = organization();
        Identifier generated = PartyTestFixtures.identifier();

        when(publicIdGenerator.generate()).thenReturn(generated);

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Organization result = repository.insert(domain);
        verify(publicIdGenerator).generate();

        assertThat(domain.getIdentifier()).isEqualTo(generated);
        assertThat(result).isSameAs(domain);
    }

    @Test
    void shouldOverideIdentifierExistingIdentifier() {
        Organization domain = organizationDomain();
        Identifier original = domain.getIdentifier();
        OrganizationEntity entity = organization();

        Identifier generated = PartyTestFixtures.identifier();

        when(publicIdGenerator.generate()).thenReturn(generated);


        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        repository.insert(domain);

        verify(publicIdGenerator).generate();

        assertThat(domain.getIdentifier()).isNotEqualTo(original);
        assertThat(domain.getIdentifier()).isEqualTo(generated);
        
    }

    @Test
    void shouldReturnDomainMappedFromSavedEntity() {
        Organization domain = organizationDomain();
        OrganizationEntity entity = organization();
        Organization expected = organizationDomain();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(expected);

        Organization result = repository.insert(domain);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void shouldMapAllEntitiesToDomain() {
        OrganizationEntity first = organization();
        OrganizationEntity second = organization();
        Organization d1 = organizationDomain();
        Organization d2 = organizationDomain();

        when(jpaRepository.findByIsDeletedFalse()).thenReturn(List.of(first, second));
        when(mapper.toDomain(first)).thenReturn(d1);
        when(mapper.toDomain(second)).thenReturn(d2);

        List<Organization> result = repository.findAll();

        assertThat(result).containsExactly(d1, d2);
    }

    @Test
    void shouldReturnEmptyListWhenNoOrganizations() {
        when(jpaRepository.findByIsDeletedFalse()).thenReturn(List.of());

        assertThat(repository.findAll()).isEmpty();
    }
}
