package com.loosie.javaallinone.project3.mycontact.configuration.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.loosie.javaallinone.project3.mycontact.domain.dto.Birthday;

import java.io.IOException;
import java.time.LocalDate;

public class BirthdaySerializer extends JsonSerializer<Birthday> {
    @Override
    public void serialize(Birthday value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value != null) {
            gen.writeObject(LocalDate.of(
                    value.getYearOfBirthday(),
                    value.getMonthOfBirthday(),
                    value.getDayOfBirthday()));
        }
    }
}
