package com.chatterMW.restController;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatter.DAO.ForumDAO;
import com.chatter.model.BlogComment;
import com.chatter.model.Forum;
import com.chatter.model.ForumComment;

@RestController
public class ForumController {

	@Autowired
	ForumDAO forumDAO;

	@GetMapping(value = "/demoForum")
	public ResponseEntity<String> demoForum() {
		return new ResponseEntity<String>("Demo Forum Rest Controller", HttpStatus.OK);
	}

	// ---------------- Add Forum -----------------------------------

	@PostMapping(value = "/addForum")
	public ResponseEntity<String> addForum(@RequestBody Forum forum) {
		forum.setCreatedDate(new Date());
		forum.setStatus("A");
		if (forumDAO.addForum(forum)) {
			return new ResponseEntity<String>("Forum added successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Forum insert failed", HttpStatus.NOT_FOUND);
		}
	}

	// -----------------------Approve Blog ----------------------------------

	@PutMapping(value = "/approveForum/{forumId}")
	public ResponseEntity<String> approveBlog(@PathVariable("forumId") int forumId) {
		System.out.println("Approved forum with forum ID: " + forumId);
		Forum forum = forumDAO.getForum(forumId);
		if (forum == null) {
			System.out.println("No forum with forum Id: " + forumId + " found for Approval");
			return new ResponseEntity<String>("No forum found for Approval", HttpStatus.NOT_FOUND);
		} else {
			forum.setStatus("A");
			forumDAO.approveForum(forum);
			return new ResponseEntity<String>("Forum with Id " + forumId + " Approved Successfully", HttpStatus.OK);
		}
	}

	// --------------------Reject Blog ----------------------------------

	@PutMapping(value = "/disapproveForum/{forumId}")
	public ResponseEntity<String> rejectBlog(@PathVariable("forumId") int forumId) {
		System.out.println("Disapprove Forum with forum ID: " + forumId);
		Forum forum = forumDAO.getForum(forumId);
		if (forum == null) {
			System.out.println("No forum with forum Id: " + forumId + " found for Approval");
			return new ResponseEntity<String>("No forum with Id " + forumId + " found for Disapproval",
					HttpStatus.NOT_FOUND);
		} else {
			forum.setStatus("NA");
			forumDAO.rejectForum(forum);
			return new ResponseEntity<String>("Forum with Id " + forumId + " Disapproved Successfully", HttpStatus.OK);
		}
	}

	// -----------------------Get Forum ------------------------------------

	@GetMapping(value = "/getForum/{forumId}")
	public ResponseEntity<String> getBlog(@PathVariable("forumId") int forumId) {
		System.out.println("In Get Forum " + forumId);
		Forum forum = forumDAO.getForum(forumId);
		if (forum == null) {
			return new ResponseEntity<String>("No forum " + forumId + " found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>("Forum with Id " + forumId + " found Successfully", HttpStatus.OK);
		}
	}

	// ----------------- List Forums ------------------------------
	@GetMapping(value = "/listForums")
	public ResponseEntity<List<Forum>> listForum() {
		List<Forum> listForums = forumDAO.listForum();
		if (listForums.size() != 0) {
			return new ResponseEntity<List<Forum>>(listForums, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Forum>>(listForums, HttpStatus.NOT_FOUND);
		}
	}

	// ------------------Update Forum -----------------------------------

	@PutMapping(value = "/updateForum/{forumId}")
	public ResponseEntity<String> updateBlog(@PathVariable("forumId") int forumId, @RequestBody Forum forum) {
		System.out.println("Updating forum " + forumId);
		Forum mForum = forumDAO.getForum(forumId);
		if (mForum == null) {
			System.out.println("Forum with forumId " + forumId + " Not Found");
			return new ResponseEntity<String>("Update forum " + forumId + " Failed", HttpStatus.NOT_FOUND);
		}

		mForum.setForumContent(forum.getForumContent());
		mForum.setForumName(forum.getForumName());
		mForum.setCreatedDate(new Date());
		// mForum.setLikes(blog.getLikes());
		mForum.setStatus(forum.getStatus());
		mForum.setUserName(forum.getUserName());

		forumDAO.updateForum(mForum);
		return new ResponseEntity<String>("Updated Forum " + forumId + " Successfully", HttpStatus.OK);
	}

	// ------------------ Delete Forum
	@DeleteMapping(value = "/deleteForum/{forumId}")
	public ResponseEntity<String> deleteBlog(@PathVariable("forumId") int forumId) {
		System.out.println("In delete forum with Id: " + forumId);
		Forum forum = forumDAO.getForum(forumId);
		if (forum == null) {
			System.out.println("No forum found to delete");
			return new ResponseEntity<String>("No forum with forum Id: " + forumId + " found to delete",
					HttpStatus.NOT_FOUND);
		} else {
			forumDAO.deleteForum(forum);
			return new ResponseEntity<String>("Forum with forum Id " + forumId + " deleted successfully",
					HttpStatus.OK);
		}
	}

	// ------------------ Add Forum Comment --------------------
	@PostMapping(value = "/addForumComment")
	public ResponseEntity<String> addForumComment(@RequestBody ForumComment forumComment) {
		System.out.println("In AddForumComment Rest method");
		forumComment.setForumId(forumComment.getForumId());
		forumComment.setCommentDate(new Date());
		forumComment.setCommentText(forumComment.getCommentText());
		forumComment.setUserName(forumComment.getUserName());

		if (forumDAO.addForumComment(forumComment)) {
			return new ResponseEntity<String>("Forum Comment Added Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Forum Comment failed", HttpStatus.NOT_FOUND);
		}
	}

	// ---------------------Get Forum Comment -----------------
	@GetMapping(value="/getForumCommentByCommentId/{commentId}")
	public ResponseEntity<ForumComment> getForumCommentByCommentId (@PathVariable("commentId") int commentId){
		ForumComment mForumComment = forumDAO.getForumComment(commentId);
		if(mForumComment == null){
			return new ResponseEntity<ForumComment>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<ForumComment>(mForumComment, HttpStatus.OK);
		}
	}
	
	// ------------------- List Forum Comments -----------------------
	@GetMapping(value="/listForumComments/{forumId}")
	public ResponseEntity<List<ForumComment>> listForumComments(@PathVariable("forumId") int forumId)
	{
		System.out.println("in list forum comments rest method");
		List<ForumComment> listComments = forumDAO.listForumComment(forumId);
		if(listComments.size() != 0){
			return new ResponseEntity<List<ForumComment>>(listComments, HttpStatus.OK);
		}else{
			return new ResponseEntity<List<ForumComment>>(HttpStatus.NOT_FOUND);
		}
	}
	
	// ------------------ Delete Forum Comment ------------------------
	@DeleteMapping(value="/deleteForumComment/{commentId}")
	public ResponseEntity<String> deleteForumComment(@PathVariable("commentId") int commentId){
		ForumComment mForumComment = forumDAO.getForumComment(commentId);
		if(mForumComment == null){
			return new ResponseEntity<String>("No forum comments found to delete", HttpStatus.NOT_FOUND);
		}
		
		forumDAO.deleteForumComment(mForumComment);
		return new ResponseEntity<String>("Forum comment " +commentId+" deleted", HttpStatus.OK);
	}
}
