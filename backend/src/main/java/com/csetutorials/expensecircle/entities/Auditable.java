package com.csetutorials.expensecircle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {

	@Column(updatable = false)
	@CreatedBy
	protected String createdBy;

	@Column
	@LastModifiedBy
	protected String updatedBy;

	@Column(updatable = false)
	@CreatedDate
	protected Long createdAt;

	@Column
	@LastModifiedDate
	protected Long updatedAt;
}
