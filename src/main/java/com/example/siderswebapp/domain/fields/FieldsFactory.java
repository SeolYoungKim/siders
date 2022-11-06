package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.Ability;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.post.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.post.update.UpdateFieldsRequest;

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
