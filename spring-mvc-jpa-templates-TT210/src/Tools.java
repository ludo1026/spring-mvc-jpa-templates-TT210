import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.generator.context.JavaBeanClass;
import org.telosys.tools.generator.context.JavaBeanClassAttribute;
import org.telosys.tools.generator.context.JavaBeanClassForeignKey;
import org.telosys.tools.generator.context.JavaBeanClassForeignKeyColumn;
import org.telosys.tools.generator.context.JavaBeanClassLink;
import org.telosys.tools.generator.context.ProjectInContext;

public class Tools {

	/**
	 * Get variable form project configuration. 
	 * @param projectConfiguration Project configuration
	 * @param variableName Variable name
	 * @return Variable
	 */
	public Variable getVariable(ProjectInContext projectConfiguration, String variableName) {
		for(Variable variable : projectConfiguration.getAllVariables()) {
			if(variable.getName().equalsIgnoreCase(variableName)) {
				return variable;
			}
		}
		return null;
	}
	
	/**
	 * Get variable valeu from project configuration
	 * @param projectConfiguration Project configuration
	 * @param variableName Variable name
	 * @return Variable value
	 */
	public String getVariableValue(ProjectInContext projectConfiguration, String variableName) {
		Variable variable = getVariable(projectConfiguration, variableName);
		if(variable == null) {
			return "";
		} else {
			return variable.getValue();
		}
	}
	
	/**
	 * Indicates if the attribute type is text
	 * @param attribute Attribute
	 * @return boolean
	 */
	public boolean isText(JavaBeanClassAttribute attribute) {
		String[] types = {"String"};
		return Arrays.asList(types).contains(attribute.getWrapperType());
	}
	
	/**
	 * Indicates if the attribute is a number
	 * @param attribute Attribute
	 * @return boolean
	 */
	public boolean isNumber(JavaBeanClassAttribute attribute) {
		String[] types = {"Integer","Long","Double","Short","Byte","Float","Number","BigInteger","BigDecimal"};
		return Arrays.asList(types).contains(attribute.getWrapperType());
	}

	/**
	 * Indicates if the attribute is a date
	 * @param attribute Attribute
	 * @return boolean
	 */
	public boolean isDate(JavaBeanClassAttribute attribute) {
		String[] types = {"Date","Calendar"};
		return Arrays.asList(types).contains(attribute.getWrapperType());
	}
	
	/**
	 * Get entity key attribute.
	 * @param entity Entity
	 * @return attribute
	 */
	public JavaBeanClassAttribute keyField(JavaBeanClass entity) {
		if(entity.hasCompositePrimaryKey()) {
			return null;
		} else {
			JavaBeanClassAttribute attribute = entity.getKeyAttributes().get(0);
			return attribute;
		}
	}
	
	/**
	 * Get entity key attribute type.
	 * @param entity Entity
	 * @return Attribute type
	 */
	public String keyType(JavaBeanClass entity) {
		if(entity.hasCompositePrimaryKey()) {
			return entity.getName()+"Id";
		} else {
			JavaBeanClassAttribute attribute = entity.getKeyAttributes().get(0);
			return attribute.formatedType(0);
		}
	}
	
	/**
	 * Get entity key attribute name.
	 * @param entity Entity
	 * @return attribute name
	 */
	public String keyName(JavaBeanClass entity) {
		if(entity.hasCompositePrimaryKey()) {
			return "id";
		} else {
			JavaBeanClassAttribute attribute = entity.getKeyAttributes().get(0);
			return attribute.formatedName(0);
		}
	}

	/**
	 * Get entity key attribute getter.
	 * @param entity Entity
	 * @return Attribute getter
	 */
	public String keyGetter(JavaBeanClass entity) {
		if(entity.hasCompositePrimaryKey()) {
			return "getId";
		} else {
			JavaBeanClassAttribute attribute = entity.getKeyAttributes().get(0);
			return attribute.getGetter();
		}
	}

	/**
	 * Get entity key attribute setter.
	 * @param entity Entity
	 * @return attribute setter.
	 */
	public String keySetter(JavaBeanClass entity) {
		if(entity.hasCompositePrimaryKey()) {
			return "setId";
		} else {
			JavaBeanClassAttribute attribute = entity.getKeyAttributes().get(0);
			return attribute.getSetter();
		}
	}
	
	/**
	 * Indicates if the primary key is an auto-generated value.
	 * @param entity Entity
	 * @param field Key attribute
	 * @return boolean
	 */
	public boolean isGeneratedValue( JavaBeanClass entity, JavaBeanClassAttribute field) {
		boolean isGeneratedValue = false;
		if( field.isKeyElement() ) {
			if( ! field.isUsedInLinkJoinColumn(entity.getLinks()) ) {
				if( field.isAutoIncremented() || field.isGeneratedValue() ) {
					isGeneratedValue = true;
				}
				if( field.formatedType(0).equals("Integer")
				 || field.formatedType(0).equals("Short")
				 || field.formatedType(0).equals("Long")
				 || field.formatedType(0).equals("Double")
				 || field.formatedType(0).equals("BigInteger")
				 || field.formatedType(0).equals("BigDecimal") ) 
				{
					isGeneratedValue = true;
				}
			}
		}
		return isGeneratedValue;
	}
	
	/**
	 * Indicates if one of these key attributes is an auto-generated value.
	 * @param entity Entity
	 * @param fields Key attributes
	 * @return boolean
	 */
	public boolean hasGeneratedValue( JavaBeanClass entity, List<JavaBeanClassAttribute> fields) {
		boolean hasGeneratedValue = false;
		for( JavaBeanClassAttribute field : fields ) {
			if( isGeneratedValue( entity, field ) ) {
				hasGeneratedValue = true;
			}
		}
		return hasGeneratedValue;
	}

	/**
	 * Indique si le champ est utilisé par un des liens.
	 * @param attribute Champ
	 * @param links Liens
	 * @return booléen
	 */
	public boolean isFieldUsedInLinks(JavaBeanClassAttribute attribute, List<JavaBeanClassLink> links) {
		boolean isFieldUsedInLinks = false;
		for( JavaBeanClassLink link : links ) {
			if( link.hasJoinColumns() ) {
				for( String joinColumn : link.getJoinColumns() ) {
					if( joinColumn.equals(attribute.getDatabaseName()) ) {
						isFieldUsedInLinks = true;
					}
				}
			}
		}
		return isFieldUsedInLinks;
	}

	/**
	 * Indique si le lien utilise un des champs.
	 * @param link Lien
	 * @param fields Champs
	 * @return booléen
	 */
	public boolean isLinkUsedByFields(JavaBeanClassLink link, List<JavaBeanClassAttribute> fields) {
		boolean isLinkUsedByFields = false;
		for( JavaBeanClassAttribute field : fields ) {
			if( link.hasJoinColumns() ) {
				for( String joinColumn : link.getJoinColumns() ) {
					if( joinColumn.equals(field.getDatabaseName()) ) {
						isLinkUsedByFields = true;
					}
				}
			}
		}
		return isLinkUsedByFields;
	}
	
	/**
	 * Retourne les liens utilisant le champ.
	 * @param links Liens
	 * @param field Champ
	 * @return liens
	 */
	public List<JavaBeanClassLink> linksForField( List<JavaBeanClassLink> links, JavaBeanClassAttribute field) {
		List<JavaBeanClassLink> linksForField = new ArrayList<JavaBeanClassLink>();
		for( JavaBeanClassLink link : links ) {
			if( link.hasJoinColumns() ) {
				for( String joinColumn : link.getJoinColumns() ) {
					if( joinColumn.equals(field.getDatabaseName()) ) {
						linksForField.add(link);
					}
				}
			}
		}
		return linksForField;
	}

	/**
	 * Retourne les liens utilisés par les champs.
	 * @param links Liens
	 * @param fields Champs
	 * @return liens
	 */
	public List<JavaBeanClassLink> linksForFields( List<JavaBeanClassLink> links, List<JavaBeanClassAttribute> fields) {
		List<JavaBeanClassLink> linksForFields = new ArrayList<JavaBeanClassLink>();
		for( JavaBeanClassAttribute field : fields ) {
			for( JavaBeanClassLink link : links ) {
				if( link.hasJoinColumns() ) {
					for( String joinColumn : link.getJoinColumns() ) {
						if( joinColumn.equals(field.getDatabaseName()) ) {
							linksForFields.add(link);
						}
					}
				}
			}
		}
		return linksForFields;
	}

	/**
	 * Champs correspondants aux liens.
	 * @param link Lien
	 * @param fields Champs
	 * @return champs
	 */
	public List<JavaBeanClassAttribute> fieldsForLink( JavaBeanClassLink link, List<JavaBeanClassAttribute> fields) {
		List<JavaBeanClassAttribute> fieldsForLink = new ArrayList<JavaBeanClassAttribute>();
		for( JavaBeanClassAttribute field : fields ) {
			if( link.hasJoinColumns() ) {
				for( String joinColumn : link.getJoinColumns() ) {
					if( joinColumn.equals(field.getDatabaseName()) ) {
						fieldsForLink.add(field);
					}
				}
			}
		}
		return fieldsForLink;
	}
	
	/**
	 * Champs correspondants aux liens
	 * @param links Liens
	 * @param fields Champs
	 * @return champs
	 */
	public List<JavaBeanClassAttribute> fieldsForLinks(List<JavaBeanClassLink> links, List<JavaBeanClassAttribute> fields) {
		List<JavaBeanClassAttribute> fieldsForLinks = new ArrayList<JavaBeanClassAttribute>();
		for( JavaBeanClassLink link : links ) {
			for( JavaBeanClassAttribute field : fields ) {
				if( link.hasJoinColumns() ) {
					for( String joinColumn : link.getJoinColumns() ) {
						if( joinColumn.equals(field.getDatabaseName()) ) {
							fieldsForLinks.add(field);
						}
					}
				}
			}
		}
		return fieldsForLinks;
	}
	
	/**
	 * Retourne la map qui a en clé l'attribut de l'entité de départ et en valeur l'attribut de l'entité cible.
	 * @param entityCurrent entité actuelle
	 * @param entityTarget entité cible
	 * @param link lien entre l'entité actuelle et l'entité cible
	 * @param fieldsOfEntityCurrent champs de l'entité actuelle à analyser
	 * @return map des attributs
	 */
	public Map<JavaBeanClassAttribute, JavaBeanClassAttribute> fieldsMappingForLink( JavaBeanClass entityCurrent, JavaBeanClass entityTarget, JavaBeanClassLink link, List<JavaBeanClassAttribute> fieldsOfEntityCurrent) {
		Map<JavaBeanClassAttribute, JavaBeanClassAttribute> fieldsMappingForLink = new HashMap<JavaBeanClassAttribute, JavaBeanClassAttribute>(); 
		if( fieldsOfEntityCurrent != null ) {
			// Link fields
			List<JavaBeanClassAttribute> fieldsForLink = fieldsForLink( link, fieldsOfEntityCurrent );
			for( JavaBeanClassAttribute fieldForLink : fieldsForLink ) {
				// Foreign keys of current entity
				for( JavaBeanClassForeignKey fk : entityCurrent.getDatabaseForeignKeys() ) {
					// Foreign key column
					for( JavaBeanClassForeignKeyColumn fkcol : fk.getColumns() ) {
						// Search foreign key column for link field
						if( fieldForLink.getDatabaseName().equals(fkcol.getColumnName()) ) {
							// Target entity field
							for( JavaBeanClassAttribute fieldOfEntityTarget : entityTarget.getAttributes() ) {
								// Search target entity field for foreign key
								if( fieldOfEntityTarget.getDatabaseName().equals(fkcol.getReferencedColumnName()) ) {
									fieldsMappingForLink.put(fieldForLink, fieldOfEntityTarget);
								}
							}
						}
					}
				}
			}
		}
		return fieldsMappingForLink;
	}

	/**
	 * Get entity links selected on attribute criteria.
	 * @param entity Entity
	 * @param criterias Criteria
	 * @return links
	 */
	public List<JavaBeanClassLink> links(JavaBeanClass entity, int... criterias) {
		if(criterias == null || criterias.length == 0) {
			return entity.getLinks();
		}
		List<JavaBeanClassAttribute> attributes = new ArrayList<JavaBeanClassAttribute>();
		if(criterias.length == 1) {
			attributes = entity.getAttributesByCriteria(criterias[0]);
		} else if(criterias.length == 2) {
			attributes = entity.getAttributesByCriteria(criterias[0], criterias[1]);
		} else if(criterias.length == 3) {
			attributes = entity.getAttributesByCriteria(criterias[0], criterias[1], criterias[2]);
		} else if(criterias.length == 4) {
			attributes = entity.getAttributesByCriteria(criterias[0], criterias[1], criterias[2], criterias[3]);
		} 
		// Récupérer les liens à partir des attributs
		return linksForFields(entity.getLinks(), attributes);
	}

	/**
	 * Get class name.
	 * @param object Object
	 * @return Class name
	 */
	public String getClassName(Object object) {
		if(object == null) {
			return "null";
		} else {
			return object.getClass().getName();
		}
	}
	
}
