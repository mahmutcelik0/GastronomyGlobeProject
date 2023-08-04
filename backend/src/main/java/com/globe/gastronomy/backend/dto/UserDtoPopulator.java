package com.globe.gastronomy.backend.dto;

import com.globe.gastronomy.backend.model.User;

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

        return userDto;
    }

    @Override
    protected User reverseConverter(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        user.setEmail(userDto.getEmail());

        return user;
    }


}
