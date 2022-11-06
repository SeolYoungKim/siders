package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.web.request.post.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.post.create.CreateTechStackRequest;

import java.util.List;
import java.util.stream.IntStream;

public class TechStackFactory {
    private static TechStack newInstance(Fields fields, CreateTechStackRequest techStackDto) {
        return TechStack.builder()
                .fields(fields)
                .stackName(techStackDto.getStackName())
                .build();
    }

    public static void saveNewTechStackToFields(List<CreateFieldsRequest> fieldsDtoList, List<Fields> fieldsList) {
        IntStream.range(0, fieldsDtoList.size())
                .forEach(i -> fieldsDtoList.get(i)
                        .getStacks()
                        .forEach(techStackDto -> newInstance(fieldsList.get(i), techStackDto)));

    }
}
