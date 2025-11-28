package com.example.demo.configuration;

import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.classic.pattern.LevelConverter;
import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.pattern.ThreadConverter;
import ch.qos.logback.core.ConsoleAppender;
import com.fasterxml.jackson.core.Base64Variant;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Runtime hints for GraalVM native image compilation.
 * Registers Logback and Jackson classes that require reflection at runtime.
 *
 * This ensures the logging framework and JSON serialization work correctly in native images.
 */
@Configuration
@ImportRuntimeHints(NativeRuntimeHintsConfiguration.ApplicationRuntimeHints.class)
public class NativeRuntimeHintsConfiguration {

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // Register Logback core classes
            hints.reflection()
                .registerType(ConsoleAppender.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.INVOKE_DECLARED_METHODS)

                // Register pattern converters (for %d, %p, %c, %m, %t, etc.)
                .registerType(DateConverter.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS)
                .registerType(LevelConverter.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS)
                .registerType(LoggerConverter.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS)
                .registerType(MessageConverter.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS)
                .registerType(ThreadConverter.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS)

                // Register Jackson classes that need reflection
                .registerType(Base64Variant.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // Register Logback configuration files as resources
            hints.resources()
                .registerPattern("logback-spring.xml")
                .registerPattern("logback.xml")
                .registerPattern("logback-test.xml")
                .registerPattern("org/springframework/boot/logging/logback/*");
        }
    }
}

