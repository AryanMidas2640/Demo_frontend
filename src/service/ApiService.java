package service;

import util.Session;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

    // ======================================
    // LOGIN (ONLY username + password)
    // ======================================
    public static String login(
            String username,
            String password) {

        try {

            URL url =
                    new URL(
                            "http://localhost:9090/api/jobs/login"
                    );

            HttpURLConnection con =
                    (HttpURLConnection)
                            url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            String json =
                    "{"
                            + "\"username\":\"" + username + "\","
                            + "\"password\":\"" + password + "\""
                            + "}";

            OutputStream os =
                    con.getOutputStream();

            os.write(json.getBytes());
            os.flush();
            os.close();

            return readResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR LOGIN";
        }
    }

    // ======================================
    // SIGNUP (username + password + role)
    // ======================================
    public static String signup(
            String username,
            String password,
            String role) {

        try {

            URL url =
                    new URL(
                            "http://localhost:9090/api/jobs/signing"
                    );

            HttpURLConnection con =
                    (HttpURLConnection)
                            url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            String json =
                    "{"
                            + "\"username\":\"" + username + "\","
                            + "\"password\":\"" + password + "\","
                            + "\"role\":\"" + role + "\""
                            + "}";

            OutputStream os =
                    con.getOutputStream();

            os.write(json.getBytes());
            os.flush();
            os.close();

            return readResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR SIGNUP";
        }
    }

    // ======================================
    // GET ALL JOBS
    // ======================================
    public static String getAllJobs() {

        return getRequest(
                "http://localhost:9090/api/jobs/all"
        );
    }

    // ======================================
    // GET JOBS BY TENANT
    // ======================================
    public static String getJobsByTenant(
            String tenantId) {

        return getRequest(
                "http://localhost:9090/api/jobs/tenant/"
                        + tenantId
        );
    }

    // ======================================
    // MY POSTED JOBS
    // ======================================
    public static String myPostedJobs() {

        return getRequest(
                "http://localhost:9090/api/jobs/tenant/"
                        + Session.tenantId
        );
    }

    // ======================================
    // SEARCH JOB BY ID
    // ======================================
    public static String getJobById(
            String jobId) {

        return getRequest(
                "http://localhost:9090/api/jobs/"
                        + jobId
        );
    }

    // ======================================
    // APPLY JOB
    // ======================================
    public static String applyJob(
            String jobId) {

        try {

            URL url =
                    new URL(
                            "http://localhost:9090/api/jobs/apply/"
                                    + jobId
                    );

            HttpURLConnection con =
                    (HttpURLConnection)
                            url.openConnection();

            con.setRequestMethod("POST");

            con.setRequestProperty(
                    "Authorization",
                    "Bearer " + Session.token
            );

            return readResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR APPLYING";
        }
    }

    // ======================================
    // MY APPLIED JOBS
    // ======================================
    public static String myAppliedJobs() {

        return getRequest(
                "http://localhost:9090/api/jobs/my-applied"
        );
    }

    // ======================================
    // VIEW APPLICANTS
    // ======================================
    public static String myApplicants() {

        return getRequest(
                "http://localhost:9090/api/jobs/my-applicants"
        );
    }

    // ======================================
    // POST JOB
    // ======================================
    public static String postJob(
            String jobId,
            String title,
            String company,
            String city,
            String type,
            String mode,
            String salary,
            String email
    ) {

        try {

            URL url =
                    new URL(
                            "http://localhost:9090/api/jobs/add"
                    );

            HttpURLConnection con =
                    (HttpURLConnection)
                            url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            con.setRequestProperty(
                    "Authorization",
                    "Bearer " + Session.token
            );

            String json =
                    "{"
                            + "\"jobId\":\"" + jobId + "\","
                            + "\"tenantId\":\"" + Session.tenantId + "\","
                            + "\"jobTitle\":\"" + title + "\","
                            + "\"companyName\":\"" + company + "\","
                            + "\"city\":\"" + city + "\","
                            + "\"jobType\":\"" + type + "\","
                            + "\"workMode\":\"" + mode + "\","
                            + "\"salary\":\"" + salary + "\","
                            + "\"email\":\"" + email + "\""
                            + "}";

            OutputStream os =
                    con.getOutputStream();

            os.write(json.getBytes());
            os.flush();
            os.close();

            return readResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR POSTING JOB";
        }
    }

    // ======================================
    // COMMON GET METHOD
    // ======================================
    private static String getRequest(
            String urlPath) {

        try {

            URL url =
                    new URL(urlPath);

            HttpURLConnection con =
                    (HttpURLConnection)
                            url.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty(
                    "Authorization",
                    "Bearer " + Session.token
            );

            return readResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR FETCHING";
        }
    }

    // ======================================
    // RESPONSE READER
    // ======================================
    private static String readResponse(
            HttpURLConnection con)
            throws Exception {

        InputStream stream;

        if (con.getResponseCode() >= 200 &&
                con.getResponseCode() < 300) {

            stream = con.getInputStream();

        } else {

            stream = con.getErrorStream();

            if (stream == null) {
                return "SERVER ERROR : "
                        + con.getResponseCode();
            }
        }

        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(stream)
                );

        String line;

        StringBuilder response =
                new StringBuilder();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();

        return response.toString();
    }
}