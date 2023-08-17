package com.globe.gastronomy.backend.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.globe.gastronomy.backend.constants.Gender;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.utils.LogUtil;
import nonapi.io.github.classgraph.json.JSONDeserializer;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDtoPopulator extends AbstractPopulator<User, UserDto> {
    @Override
    public UserDto generateTarget() {
        return new UserDto();
    }

    @Override
    public User generateSource() {
        return new User();
    }

    @Override
    protected UserDto populate(User user, UserDto userDto) {
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map(e -> new RoleDtoPopulator().populate(e)).collect(Collectors.toSet()));
        return userDto;
    }

    @Override
    protected User reverseConverter(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles().stream().map(e -> new RoleDtoPopulator().reverseConverter(e)).collect(Collectors.toSet()));
        return user;
    }

    public UserDto stringConverter(String userStr) {
        String content = userStr.substring(userStr.indexOf("{") + 1, userStr.lastIndexOf("}"));

        String[] keyValuePairs = content.split(", ");
        Map<String, String> keyValueMap = new HashMap<>();

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split("=");
            String lastPart = parts[1].contains("'") ?
                    parts[1].substring(parts[1].indexOf("'") + 1, parts[1].lastIndexOf("'")) :
                    parts[1];
            keyValueMap.put(parts[0], lastPart);
        }

        UserDto userDto = new UserDto();
        userDto.setFirstName(keyValueMap.get("firstName"));
        userDto.setLastName(keyValueMap.get("lastName"));
        userDto.setGender(Gender.valueOf(keyValueMap.get("gender")));
        userDto.setEmail(keyValueMap.get("email"));

        return userDto;

    }
}