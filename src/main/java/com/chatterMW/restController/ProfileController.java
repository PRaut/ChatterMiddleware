package com.chatterMW.restController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chatter.DAO.ProfileUpdateDAO;
import com.chatter.model.ProfilePicture;
import com.chatter.model.User;

@RestController
public class ProfileController {

	@Autowired
	ProfileUpdateDAO profileUpdateDAO;

	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadPicture(@RequestParam(value = "file1") CommonsMultipartFile fileupload,
			HttpSession session, User user) {
		System.out.println("m in upload function");
		String user1 = (String) session.getAttribute("userName");

		if (user == null) {
			System.out.println("Null User Name");
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		} else {

			ProfilePicture profilePicture = new ProfilePicture();
			System.out.println("Profile Picture Found");
			profilePicture.setLoginName(user1);

			profilePicture.setImage(fileupload.getBytes());
			System.out.println("Profile Image Set");
			profileUpdateDAO.save(profilePicture);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getImage/{loginName}")
	public @ResponseBody byte[] getProfilePic(@PathVariable("loginName") String loginName) {
		System.out.println("I AM IN GETIMAGE FUN "+loginName);
		ProfilePicture profilePicture = profileUpdateDAO.getProfilePicture(loginName);

		if (profilePicture == null) {
			System.out.println("NO IMAGE FOUND ");
			return null;
		} else {
			return profilePicture.getImage();
		}
	}

}