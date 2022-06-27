package org.clematis.mt.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition to exclude configurations from profiles
 * @author Anton Troshin
 */
public class NotLocalEnvironment implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return !environment.acceptsProfiles(Profiles.of("local"));
    }
}
