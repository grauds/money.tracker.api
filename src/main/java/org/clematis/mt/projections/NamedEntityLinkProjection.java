package org.clematis.mt.projections;

/**
 * Due to a <a href="https://github.com/spring-projects/spring-data-rest/issues/1620">Spring Data Issue</a>
 * we need a projection to use in repository search methods
 */
public interface NamedEntityLinkProjection {

    int getId();

    String getName();
}
