package pb.javab.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pb.javab.daos.ICarDao;
import pb.javab.models.Car;

import java.io.IOException;

@WebServlet(name = "CarController", urlPatterns = {"/car/list", "/car/edit/*", "/car/create", "/car/delete/*", "/car/details/*"})
public class CarController extends GenericController<Car, ICarDao> {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        var path = req.getServletPath();
        switch (path) {
            case "/car/list":
                handleList(req, res);
                break;
            case "/car/details":
                handleDetails(req, res);
                break;
            case "/car/delete":
                handleDelete(req, res);
                break;
            case "/car/create":
            case "/car/edit":
                req.setAttribute("formAction", path);
                req.getRequestDispatcher("/WEB-INF/views/car/carForm.xhtml").forward(req, res);
                break;
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    }

    private void handleDetails(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    }

    private void handleList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        dao.save(new Car());
        var cars = dao.getAll();
        req.setAttribute("carList", cars);
        req.getRequestDispatcher("/WEB-INF/views/car/list.xhtml").forward(req, res);
    }

}