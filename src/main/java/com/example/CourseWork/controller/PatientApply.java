package com.example.CourseWork.controller;

import com.example.CourseWork.helpers.Utils;
import com.example.CourseWork.model.MQList;
import com.example.CourseWork.model.MedicalQueue;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(name = "PatientApply", value = "/patientApply")
public class PatientApply extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] ids = request.getParameterValues("id");
        MQList mqList = MQList.getInstance();
        String username = String.valueOf(request.getSession().getAttribute("username"));
        if (ids != null && ids.length > 0) {
            for (String doctorName : ids) {
                Optional<MedicalQueue> queue = mqList.getQueueByDoctor(doctorName);
                queue.ifPresent(medicalQueue -> medicalQueue.addLast(username));
            }
        }
        ArrayList<MedicalQueue> patientList = mqList.getQueuesWithPatient(username);
        ArrayList<MedicalQueue> noPatientList = mqList.getQueuesWithoutPatient(username);
        Utils.save(mqList);
        request.getSession().setAttribute("patientList", patientList);
        request.getSession().setAttribute("noPatientList", noPatientList);
        getServletContext().getRequestDispatcher("/WEB-INF/view/patientInfo.jsp").forward(request, response);
    }
}
