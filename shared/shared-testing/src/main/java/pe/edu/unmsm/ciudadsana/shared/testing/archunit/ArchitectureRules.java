package pe.edu.unmsm.ciudadsana.shared.testing.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public final class ArchitectureRules {

    private ArchitectureRules() {}

    public static ArchRule domainMustNotDependOnSpring() {
        return noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "jakarta.transaction..",
                        "com.fasterxml.jackson.."
                )
                .because("El dominio debe ser puro Java sin dependencias de framework");
    }

    public static ArchRule applicationMustNotDependOnInfrastructure() {
        return noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..")
                .because("Application no puede depender de infrastructure");
    }

    public static ArchRule interfacesMustNotAccessRepositoriesDirectly() {
        return noClasses()
                .that().resideInAPackage("..interfaces..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.repository..")
                .because("Las interfaces REST no pueden acceder directamente a repositorios JPA");
    }

    public static ArchRule controllersMustBeInInterfacesLayer() {
        return classes()
                .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().resideInAPackage("..interfaces.rest..")
                .because("Los controllers REST solo pueden estar en la capa interfaces");
    }

    public static ArchRule jpaEntitiesMustBeInInfrastructure() {
        return classes()
                .that().areAnnotatedWith("jakarta.persistence.Entity")
                .should().resideInAPackage("..infrastructure.persistence.entity..")
                .because("Las entidades JPA solo pueden estar en infrastructure");
    }

    public static ArchRule domainMustNotUseLombok() {
        return noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("lombok..")
                .because("El dominio no debe usar Lombok");
    }

    public static void assertAll(JavaClasses classes) {
        domainMustNotDependOnSpring().check(classes);
        applicationMustNotDependOnInfrastructure().check(classes);
        interfacesMustNotAccessRepositoriesDirectly().check(classes);
        controllersMustBeInInterfacesLayer().check(classes);
        jpaEntitiesMustBeInInfrastructure().check(classes);
        domainMustNotUseLombok().check(classes);
    }
}
