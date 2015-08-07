package com.wizsharing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 组和资源的关联表
 * @author ZML
 *
 */

@Entity
@Table(name = "T_GROUP_RESOURCE")
public class GroupAndResource implements Serializable {

	private static final long serialVersionUID = 5606409519598928351L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	//GenerationType.AUTO 有程序决定生成主键
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_GR_ID", allocationSize = 1)
	@Column(name = "ID", length = 5, nullable = false, unique = true)
    private Integer id;

    @Column(name = "group_id", length = 50, nullable = false)
    private Integer groupId;

    @Column(name = "resource_id", length = 50, nullable = false)
    private Integer resourceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
    
}
