package de.bildwerkmedien.fluidqr.server;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.bildwerkmedien.fluidqr.server");

        noClasses()
            .that()
                .resideInAnyPackage("de.bildwerkmedien.fluidqr.server.service..")
            .or()
                .resideInAnyPackage("de.bildwerkmedien.fluidqr.server.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..de.bildwerkmedien.fluidqr.server.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
