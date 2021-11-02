package com.app.lms.util;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.app.lms.model.PackageSection;
import com.app.lms.model.SubscriptionPackage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class SubPkgJsonSerializer extends JsonSerializer<SubscriptionPackage> {

	@Override
	public void serialize(SubscriptionPackage pkg, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("packageId", pkg.getPackageId());
		gen.writeStringField("packageName", pkg.getPackageName());
		gen.writeNumberField("fees", pkg.getFees());
		gen.writeArrayFieldStart("sections");
		for (PackageSection ps : pkg.getPackageSection()) {
			gen.writeStartObject();
			gen.writeStringField("sectionId", ps.getSection().getSectionId());
			gen.writeNumberField("noOfBooks", ps.getNumberOfBooks());
			gen.writeEndObject();
		}
		gen.writeEndArray();
		gen.writeEndObject();
	}

}
