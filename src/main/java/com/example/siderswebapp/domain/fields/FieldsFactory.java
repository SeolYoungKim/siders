package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.post.create.CreateFieldsRequest;

import java.util.ArrayList;
import java.util.List;

public class FieldsFactory {
    private static Fields newInstance(Post post, CreateFieldsRequest fieldsRequest) {
        return Fields.builder()
                .post(post)
                .fieldsName(fieldsRequest.getFieldsName())
                .recruitCount(fieldsRequest.getRecruitCount())
                .totalAbility(fieldsRequest.totalAbilityToEnum())
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
