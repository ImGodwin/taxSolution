package Godwin.taxSolution.controllers;

import Godwin.taxSolution.entities.City;
import Godwin.taxSolution.exceptions.BadRequestException;
import Godwin.taxSolution.payloads.CityDTO;
import Godwin.taxSolution.service.CityService;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/public/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("")
    public Page<City> getCities(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "15") int size,
                                @RequestParam(defaultValue = "id") String orderBy,
                                @RequestParam(defaultValue = "true") boolean ascending){
        return cityService.getAllCities(page, size, orderBy, ascending);
    }

    @GetMapping(value = "/{id}")
    public City findById(@PathVariable UUID id) {
        return cityService.findById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public City saveNewCity(@RequestBody @Validated CityDTO newCity, BindingResult validation){
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }
        return cityService.saveCity(newCity);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public City findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated CityDTO body, BindingResult validation){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return cityService.findCityByIdAndUpdate(id, body);
    }

    @PostMapping("/{id}/uploadPhoto")
    public City uploadEmployeeImage(@PathVariable UUID id, @RequestParam("file") MultipartFile body) throws IOException {
        System.out.println(body.getSize());
        System.out.println(body.getContentType());
        String uploadImageUrl = cityService.uploadPicture(body);
        return cityService.updateImg(id, uploadImageUrl);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id){
        cityService.findCityByIdAndDelete(id);
    }


}
