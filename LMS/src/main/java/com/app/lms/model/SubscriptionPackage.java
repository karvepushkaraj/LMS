package com.app.lms.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "packages")
public class SubscriptionPackage {

	@Id
	private int packageId;
	@Column(length = 50)
	private String packageName;
	private float fees, deposite;
	@OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL)
	private Set<PackageSection> packageSection;
	@OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL)
	private Set<Subscription> subscriptions;
	
	public SubscriptionPackage() {
		super();
	}

	public SubscriptionPackage(int packageId, String packageName, float fees, float deposite) {
		this.packageId = packageId;
		this.packageName = packageName;
		this.fees = fees;
		this.deposite = deposite;
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

	public float getDeposite() {
		return deposite;
	}

	public void setDeposite(float deposite) {
		this.deposite = deposite;
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
		return "Package [packageId=" + packageId + ", packageName=" + packageName + ", fees=" + fees + ", deposite="
				+ deposite + "]";
	}

}
