package com.itis403.app.service;

import com.itis403.app.dao.UserDao;
import com.itis403.app.dao.ArtistProfileDao;
import com.itis403.app.dao.LabelProfileDao;
import com.itis403.app.model.User;
import com.itis403.app.model.ArtistProfile;
import com.itis403.app.model.LabelProfile;
import com.itis403.app.util.PasswordHasher;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;
    private final ArtistProfileDao artistProfileDao;
    private final LabelProfileDao labelProfileDao;

    public UserService(UserDao userDao, ArtistProfileDao artistProfileDao, LabelProfileDao labelProfileDao) {
        this.userDao = userDao;
        this.artistProfileDao = artistProfileDao;
        this.labelProfileDao = labelProfileDao;
    }

    public User registerUser(String username, String email, String password, User.UserRole role) {
        // Check if user already exists
        if (userDao.findByUsername(username).isPresent() || userDao.findByEmail(email).isPresent()) {
            return null;
        }

        String passwordHash = PasswordHasher.hashPassword(password);
        User user = new User(username, email, passwordHash, role);
        userDao.save(user);

        // Create profile based on role
        if (role == User.UserRole.ARTIST) {
            ArtistProfile profile = new ArtistProfile(user.getId(), username, null, null);
            artistProfileDao.save(profile);
        } else {

            LabelProfile profile = new LabelProfile(user.getId(), username, null, null, null);
            labelProfileDao.save(profile);
        }

        return user;
    }

    public ArtistProfile getArtistProfile(Long userId) {
        return artistProfileDao.findByUserId(userId).orElse(null);
    }

    public LabelProfile getLabelProfile(Long userId) {
        return labelProfileDao.findByUserId(userId).orElse(null);
    }

    public boolean saveArtistProfile(ArtistProfile profile) {
        try {
            artistProfileDao.save(profile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveLabelProfile(LabelProfile profile) {
        try {
            labelProfileDao.save(profile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateUserProfile(Long userId, String username, String email) {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent() && !userOpt.get().getId().equals(userId)) {
            throw new RuntimeException("Username already taken");
        }

        userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent() && !userOpt.get().getId().equals(userId)) {
            throw new RuntimeException("Email already taken");
        }

        User user = userDao.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        user.setEmail(email);
        userDao.update(user);
    }
}