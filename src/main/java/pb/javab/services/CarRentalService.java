package pb.javab.services;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import pb.javab.daos.ICarRentalDao;
import pb.javab.models.CarRental;
import pb.javab.models.CarRentalStatus;

import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class CarRentalService implements ICarRentalService {
    private ICarRentalDao carRentalDao;

    private List<CarRental> carRentalToBePayed;

    @PostConstruct
    @Inject
    public void init(ICarRentalDao carRentalDao) {
        this.carRentalDao = carRentalDao;
        // get unpaid cars from db at startup
        carRentalToBePayed = new ArrayList<>(carRentalDao.getByStatus(CarRentalStatus.NOT_PAID));
    }

    @Override
    public boolean rent(CarRental carRental) {
        carRentalDao.save(carRental);
        carRentalToBePayed.add(carRental);
        return true;
    }

    @Override
    public boolean pay(Long id) {
        var rental = getCarRentalFromList(id);
        if (rental == null)
            return false;

        rental.setStatus(CarRentalStatus.PAID);
        carRentalDao.update(rental);

        carRentalToBePayed.remove(rental);
        return true;
    }

    @Override
    public boolean cancel(Long id) {
        var rental = getCarRentalFromList(id);
        if (rental == null)
            return false;

        rental.setStatus(CarRentalStatus.CANCELED);
        carRentalDao.update(rental);

        carRentalToBePayed.remove(rental);
        return true;
    }

    private CarRental getCarRentalFromList(Long id) {
        return carRentalToBePayed.stream().filter(c -> c.getId().equals(id)).findAny().orElse(null);
    }

    // TODO
    @Schedule(hour = "*/3", minute = "*", second = "*", persistent = false)
    private void cancelNotPayedReservations() {
        // TODO wysylanie maila
    }
}
