package com.globe.gastronomy.backend.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.utils.LogUtil;
import nonapi.io.github.classgraph.json.JSONDeserializer;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.lang.reflect.InvocationTargetException;
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
        try {
            return UserDto.class.getConstructor(new Class[]{String.class}).newInstance(userStr);


        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
