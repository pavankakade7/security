package com.example.demo.entity;


import java.time.LocalDateTime;

import com.example.demo.domain.RequestContext;
import com.example.demo.exception.ApiException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static java.time.LocalDateTime.now;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Auditable {
        @Id
        @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "primary_key_seq")
        @Column(name = "id", updatable = false)
        private Long id;
        private String referencedId = new AlternativeJdkIdGenerator().generateId().toString();
        @NotNull
        private Long createdBy;
        @NotNull
        private Long updatedBy;
        @NotNull
        @CreatedDate
        @Column(name = "created_at", updatable = false, nullable = false)
        private LocalDateTime createdAt;
        @CreatedDate
        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;
        
        

        public Long getId() {
			return id;
		}



		public void setId(Long id) {
			this.id = id;
		}



		public String getReferencedId() {
			return referencedId;
		}



		public void setReferencedId(String referencedId) {
			this.referencedId = referencedId;
		}



		public Long getCreatedBy() {
			return createdBy;
		}



		public void setCreatedBy(Long createdBy) {
			this.createdBy = createdBy;
		}



		public Long getUpdatedBy() {
			return updatedBy;
		}



		public void setUpdatedBy(Long updatedBy) {
			this.updatedBy = updatedBy;
		}



		public LocalDateTime getCreatedAt() {
			return createdAt;
		}



		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}



		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}



		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}



		@PrePersist
        public void beforePersist(){
                var userId = RequestContext.getUserId();
                if(userId == null) {
                    throw new ApiException("Cannot persist entity without user ID in request context for this thread");
                }
                        setCreatedAt(now());
                        setCreatedBy(userId);
                        setUpdatedBy(userId);
                        setUpdatedAt(now());

        }

        @PreUpdate
        public void beforeUpdate(){
                var userId = RequestContext.getUserId();
                if(userId == null) {
                    throw new ApiException("Cannot update entity without user ID in request context for this thread");
                }
                        setUpdatedAt(now());
                        setUpdatedBy(userId);


        }


}


























































