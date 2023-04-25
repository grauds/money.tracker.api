package org.clematis.mt.dto;

/**
 * Due to a <a href="https://github.com/spring-projects/spring-data-rest/issues/1620">Spring Data Issue</a>
 * we need a DTO to use in repository search methods
 *
 * @author Anton Troshin
 */
public interface NamedEntityLink {

    int getId();

    String getName();
}
