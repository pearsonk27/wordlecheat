package com.wordlecheat.ui;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.stereotype.Component;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.OpenAPI;

@Component
public class OpenApiTagsCustomizer extends SpecFilter implements OpenApiCustomiser {

	@Override
	public void customise(OpenAPI openApi) {
		// remove the property reference controller
		openApi.getPaths().entrySet().removeIf(path -> path.getValue().readOperations().stream().anyMatch(
				operation -> operation.getTags().stream().anyMatch(tag -> tag.endsWith("property-reference-controller") || tag.equals("profile-controller"))));
		// rename the operation tags
		openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> {
					String tagName = operation.getTags().get(0);
					// rename the entity-controller tags
					if (tagName.endsWith("entity-controller")) {
						String entityName = tagName.substring(0, tagName.length() - 18);
                        if (entityName.equals("dictionary-entry")) {
                            entityName = "dictionary";
                        }
						// Replace with the new tag value
						operation.getTags().set(0, entityName);
					}
					// rename the search-controller tags
					else if (tagName.endsWith("search-controller")) {
						String entityName = tagName.substring(0, tagName.length() - 18);
                        if (entityName.equals("dictionary-entry")) {
                            entityName = "dictionary";
                        }
						// Replace with the new tag value
						operation.getTags().set(0, entityName);
					}
				});
		removeBrokenReferenceDefinitions(openApi);
	}
}
