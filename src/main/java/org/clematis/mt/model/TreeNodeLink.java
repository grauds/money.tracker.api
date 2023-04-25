package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * This is a link for the lists of tree node objects
 * @author Anton Troshin
 */
@Projection(name = "TreeNodeLink", types = {TreeNode.class})
public interface TreeNodeLink {

    @Value("#{target.id}")
    long getId();

    String getName();


}
