package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.web.request.post.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.post.create.CreateTechStackRequest;
import com.example.siderswebapp.web.request.post.update.UpdateTechStackRequest;

import java.util.List;
import java.util.stream.IntStream;

public class TechStackFactory {
    public static TechStack newInstance(Fields fields, UpdateTechStackRequest techStackDto) {
        return newInstance(fields, techStackDto.getStackName());
    }

    private static TechStack newInstance(Fields fields, CreateTechStackRequest techStackDto) {
        return newInstance(fields, techStackDto.getStackName());
    }

    private static TechStack newInstance(Fields fields, String stackName) {
        return TechStack.builder()
                .fields(fields)
                .stackName(stackName)
                .build();
    }

    public static void saveNewTechStackToFields(List<CreateFieldsRequest> fieldsDtoList, List<Fields> fieldsList) {
        IntStream.range(0, fieldsDtoList.size())
                .forEach(i -> fieldsDtoList.get(i)
                        .getStacks()
                        .forEach(techStackDto -> newInstance(fieldsList.get(i), techStackDto)));

    }
}
