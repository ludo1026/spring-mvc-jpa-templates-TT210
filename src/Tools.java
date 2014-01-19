import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.context.JavaBeanClass;
import org.telosys.tools.generator.context.JavaBeanClassAttribute;
import org.telosys.tools.generator.context.JavaBeanClassForeignKey;
import org.telosys.tools.generator.context.JavaBeanClassForeignKeyColumn;
import org.telosys.tools.generator.context.JavaBeanClassLink;

public class Tools {
	
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
	 * Get attribute names in the entity view model for this link.
	 * @param entityCurrent current entity
	 * @param entityTarget target entity
	 * @param link link from current entity to the target entity
	 * @param fieldsOfEntityCurrent filtered attributes list of current entity
	 * @return view model attributes name
	 */
	public List<String> fieldsViewModelForLink( JavaBeanClass entityCurrent, JavaBeanClass entityTarget, JavaBeanClassLink link, List<JavaBeanClassAttribute> fieldsOfEntityCurrent) {
		List<String> fieldsViewModelForLink = new ArrayList<String>();
		Map<JavaBeanClassAttribute, JavaBeanClassAttribute> fieldsMappingForLink = new HashMap<JavaBeanClassAttribute, JavaBeanClassAttribute>();
		for( JavaBeanClassAttribute fieldOfEntityTarget : fieldsMappingForLink.values() ) {
			fieldsViewModelForLink.add(entityCurrent.getName()+capitalize(fieldOfEntityTarget.formatedName(0)));
		}
		return fieldsViewModelForLink;
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
	 * Uncapitalize.
	 * @param str String
	 * @return String
	 */
	public String uncapitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if(str.length() == 1) {
			return str.toLowerCase();
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * Capitalize.
	 * @param str String
	 * @return String
	 */
	public String capitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if(str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Uncapitalize.
	 * @param str String
	 * @return String
	 */
	public String M(String str) {
		return capitalize(str);
	}
	
	/**
	 * Capitalize.
	 * @param str String
	 * @return String
	 */
	public String m(String str) {
		return uncapitalize(str);
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
