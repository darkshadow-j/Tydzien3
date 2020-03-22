package pl.pawel.projektapp;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cars")
public class CarAPI {

    private List<Car> carList;

    public CarAPI() {
        carList = new ArrayList<>();
        carList.add(new Car(1L, "BMW", "E36", "yellow"));
        carList.add(new Car(2L, "Peguot", "407", "blue"));
        carList.add(new Car(3L, "Renault", "Thalia","blue"));
    }

    //do pobierania wszystkich pozycji
    //do pobierania elementu po jego id
    //do pobierania elementów w określonym kolorze (query)
    @GetMapping
    public ResponseEntity<List<Car>> getCarByIDorColor(@RequestHeader(value = "id", required = false, defaultValue = "0") long id,
                                          @RequestHeader(value = "color", required = false) String color){
        List<Car> carf;
        if(id!=0) {
            carf = carList.stream().filter(car -> car.getId() == id).collect((Collectors.toList()));
        if (!carf.isEmpty()) {
                return new ResponseEntity<>(carf, HttpStatus.OK);
             }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(color!=null){
            carf=carList.stream().filter(car -> car.getColor().equals(color)).collect(Collectors.toList());
            if(!carf.isEmpty()){
                return new ResponseEntity<>(carf, HttpStatus.OK);
            }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    //do dodawania pozycji
    @PostMapping
    public ResponseEntity AddCar(@RequestBody Car car){
        if(carList.add(car));
        {
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    //do modyfikowania pozycji

    @PutMapping
    public ResponseEntity EditCar(@RequestBody Car newCar){
        Optional<Car> carf = carList.stream().filter(car -> car.getId()  == newCar.getId()).findFirst();
        if(carf.isPresent()){
            carList.remove(carf.get());
            carList.add(newCar);
            return new ResponseEntity<>(carf.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //do modyfikowania pół
    @PatchMapping("/{id}")
    public ResponseEntity EditCarPart(@RequestBody Map<String, Object> updates, @PathVariable long id){
        Optional<Car> carf = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(carf.isPresent()){
            if(updates.containsKey("model")){
                carf.get().setModel((String) updates.get("model"));
            }
            if(updates.containsKey("color")){
                carf.get().setColor((String) updates.get("color"));
            }
            if(updates.containsKey("mark")){
                carf.get().setMark((String) updates.get("mark"));
            }
            return  new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //do usuwania jeden pozycji
    @DeleteMapping("/{id}")
    public ResponseEntity DeleteCar(@PathVariable long id){
        Optional<Car> carf = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(carf.isPresent())
        {
            carList.remove(carf.get());
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
