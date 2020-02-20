package com.example.demop.utils;
import com.example.demop.model.FormSubmissionDTO;
import com.example.demop.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class Utils {
    public static String getUsernameFromRequest(HttpServletRequest request, TokenUtils tokenUtils) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }

        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    public static HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

}
