package seven.bsh.net.parser;

import seven.bsh.db.entity.Project;
import seven.bsh.net.entity.ProjectAttributesEntity;
import seven.bsh.net.entity.ProjectEntity;
import seven.bsh.net.entity.ProjectRelationshipsEntity;

public class ProjectParser extends Parser<ProjectEntity, Project> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public Project parseOne(ProjectEntity entity) {
        ProjectAttributesEntity attributes = entity.getAttributes();
        Project model = new Project();
        model.setId(entity.getId());
        model.setName(attributes.getName());

        ProjectRelationshipsEntity relationships = entity.getRelationships();
        if (relationships != null) {
            model.setChecklistIds(relationships.getChecklists());
        }
        return model;
    }
}
