package com.app.lms.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SubscriptionPackage {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packageIdSequence")
	@SequenceGenerator(name = "packageIdSequence", initialValue = 1, allocationSize = 1)
	private int packageId;

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String packageName;

	@NotNull
	@Min(value = 1)
	private float fees;

	@JsonIgnore
	@OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PackageSection> packageSection;

	@JsonIgnore
	@OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL)
	private Set<Subscription> subscriptions;

	public SubscriptionPackage() {
		super();
	}

	public SubscriptionPackage(int packageId, String packageName, float fees) {
		this.packageId = packageId;
		this.packageName = packageName;
		this.fees = fees;
	}

	public int getPackageId() {
		return packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public float getFees() {
		return fees;
	}

	public void setFees(float fees) {
		this.fees = fees;
	}

	public Set<PackageSection> getPackageSection() {
		return Collections.unmodifiableSet(packageSection);
	}

	public Set<Subscription> getSubscriptions() {
		return Collections.unmodifiableSet(subscriptions);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + packageId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubscriptionPackage other = (SubscriptionPackage) obj;
		if (packageId != other.packageId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Package [packageId=" + packageId + ", packageName=" + packageName + ", fees=" + fees + "]";
	}

}
