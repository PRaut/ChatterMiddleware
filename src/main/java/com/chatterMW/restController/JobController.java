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

import com.chatter.DAO.JobDAO;
import com.chatter.model.ApplyJob;
import com.chatter.model.Blog;
import com.chatter.model.Job;

@RestController
public class JobController {
	@Autowired
	JobDAO jobDAO;

	// ----------------------- Add Job -------------------
	@PostMapping(value = "/addJob")
	public ResponseEntity<String> addBlog(@RequestBody Job job) {
		if (jobDAO.addJob(job)) {
			return new ResponseEntity<String>("Job Added Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Job insertion failed", HttpStatus.NOT_FOUND);
		}
	}

	// ---------------------- Update Job ---------------------
	@PutMapping(value = "/updateJob/{jobId}")
	public ResponseEntity<String> updateJob(@PathVariable("jobId") int jobId, @RequestBody Job job) {
		Job mJob = jobDAO.getJob(jobId);
		mJob.setJobTitle(job.getJobTitle());
		mJob.setJobDescription(job.getJobDescription());
		mJob.setSalary(job.getSalary());
		mJob.setNoOfOpenings(job.getNoOfOpenings());
		//mJob.setJobDesignation(job.getJobDesignation());
		mJob.setJobLocation(job.getJobLocation());
		mJob.setCompany(job.getCompany());
		mJob.setLastDateToApply(job.getLastDateToApply());

		if (jobDAO.updateJob(mJob)) {
			return new ResponseEntity<String>("Job updated successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Update job failed", HttpStatus.NOT_FOUND);
		}
	}

	// --------------------------- Get Job by Id -----------------

	@GetMapping(value = "/getJob/{jobId}")
	public ResponseEntity<Job> getJob(@PathVariable("jobId") int jobId) {
		System.out.println("In Get Job " + jobId);
		Job job = jobDAO.getJob(jobId);
		if (job == null) {
			return new ResponseEntity<Job>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Job>(job, HttpStatus.OK);
		}
	}

	// ------------------ List jobs ---------------------
	@GetMapping(value = "/listJobs")
	public ResponseEntity<List<Job>> listJob() {
		List<Job> listJobs = jobDAO.listJob();
		if (listJobs.size() != 0) {
			return new ResponseEntity<List<Job>>(listJobs, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Job>>(HttpStatus.NOT_FOUND);
		}
	}

	// ----------------------------- Delete Job ------------------------
	@DeleteMapping(value = "/deleteJob/{jobId}")
	public ResponseEntity<String> deleteJob(@PathVariable("jobId") int jobId) {
		System.out.println("Delete blog with blog Id: " + jobId);
		Job job = jobDAO.getJob(jobId);
		if (job == null) {
			System.out.println("No job " + jobId + " found to delete");
			return new ResponseEntity<String>("No job with jobl Id: " + jobId + " found to delete",
					HttpStatus.NOT_FOUND);
		} else {
			jobDAO.deleteJob(job);
			return new ResponseEntity<String>("Job with job Id " + jobId + " deleted successfully", HttpStatus.OK);
		}
	}

	// ------------------------------ Apply Job --------------------------
	@PostMapping(value = "/applyJob")
	public ResponseEntity<String> addJob(@RequestBody ApplyJob applyJob) {
		applyJob.setApplyDate(new Date());
		applyJob.setJobId(1);
		if (jobDAO.applyJob(applyJob)) {
			System.out.println("==========> ApplyJob details added successfully..");
			return new ResponseEntity<String>("ApplyJob Added- Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Applyjob failed", HttpStatus.NOT_FOUND);
		}
	}

	// ---------------------List All Applied Jobs -------------------------
	@GetMapping(value = "/listApplyJobs")
	public ResponseEntity<List<ApplyJob>> getListApplyJobs() {
		System.out.println("rest controller in Applyjob list");
		List<ApplyJob> listApplyJobs = jobDAO.getAllApplicationJobDetails();
		if (listApplyJobs.size() != 0)
			return new ResponseEntity<List<ApplyJob>>(listApplyJobs, HttpStatus.OK);
		else
			return new ResponseEntity<List<ApplyJob>>(HttpStatus.NOT_FOUND);
	}
}
