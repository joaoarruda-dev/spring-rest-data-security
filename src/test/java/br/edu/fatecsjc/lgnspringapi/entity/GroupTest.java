package br.edu.fatecsjc.lgnspringapi.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void testGroupEntity() {
        // Test Builder
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");

        Group group = Group.builder()
                .id(1L)
                .name("Test Group")
                .organization(organization)
                .members(new ArrayList<>())
                .build();

        assertNotNull(group);
        assertEquals(1L, group.getId());
        assertEquals("Test Group", group.getName());
        assertEquals(organization, group.getOrganization());
        assertTrue(group.getMembers().isEmpty());

        // Test Setters
        group.setName("Updated Group");
        assertEquals("Updated Group", group.getName());

        group.setOrganization(null);
        assertNull(group.getOrganization());

        group.setMembers(null);
        assertNull(group.getMembers());
    }
}