package com.example.siderswebapp.post.domain;

import com.example.siderswebapp.post.application.dto.create.CreateFieldsRequest;
import com.example.siderswebapp.post.application.dto.update.UpdateFieldsRequest;

import java.util.ArrayList;
import java.util.List;

public class FieldsFactory {
    public static Fields newInstance(Post post, UpdateFieldsRequest fieldsRequest) {
        return newInstance(
                post,
                fieldsRequest.getFieldsName(),
                fieldsRequest.getRecruitCount(),
                fieldsRequest.totalAbilityToEnum()
        );
    }

    private static Fields newInstance(Post post, CreateFieldsRequest fieldsRequest) {
        return newInstance(
                post,
                fieldsRequest.getFieldsName(),
                fieldsRequest.getRecruitCount(),
                fieldsRequest.totalAbilityToEnum()
        );
    }

    private static Fields newInstance(Post post, String fieldsName, Integer recruitCount, Ability totalAbility) {
        return Fields.builder()
                .post(post)
                .fieldsName(fieldsName)
                .recruitCount(recruitCount)
                .totalAbility(totalAbility)
                .build();
    }

    // Post에 새로운 Fields를 할당
    public static List<Fields> fieldsList(List<CreateFieldsRequest> fieldsRequests, Post post) {
        List<Fields> results = new ArrayList<>();
        for (CreateFieldsRequest fieldsRequest : fieldsRequests) {
            results.add(newInstance(post, fieldsRequest));
        }

        return results;
    }
}
