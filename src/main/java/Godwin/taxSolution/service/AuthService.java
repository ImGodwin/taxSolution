package Godwin.taxSolution.service;

import Godwin.taxSolution.entities.City;
import Godwin.taxSolution.entities.Role;
import Godwin.taxSolution.entities.TaxPersonnel;
import Godwin.taxSolution.exceptions.BadRequestException;
import Godwin.taxSolution.exceptions.UnauthorizedException;
import Godwin.taxSolution.payloads.TaxPersonnelDTO;
import Godwin.taxSolution.payloads.TaxPersonnelLoginDTO;
import Godwin.taxSolution.repository.TaxPersonneRepository;
import Godwin.taxSolution.springSecurity.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private TaxPersonnelService taxPersonnelService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private CityService cityService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private TaxPersonneRepository taxPersonneRepository;

    public String authTaxPeronnel(TaxPersonnelLoginDTO body){

        TaxPersonnel singleTaxPersonnel = taxPersonnelService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), singleTaxPersonnel.getPassword())){
            return jwtTools.createToken(singleTaxPersonnel);
        }else {
            throw new UnauthorizedException("Not registered");
        }
    }

    public TaxPersonnel saveTaxPersonnel(TaxPersonnelDTO newTaxPersonnel){

        City addCity = cityService.findCityByName(newTaxPersonnel.cityName());
        taxPersonneRepository.findByEmail(newTaxPersonnel.email()).ifPresent(newTaxPerson -> {
            throw new BadRequestException("The email " + newTaxPerson.getEmail() + " added is already is use");
        });

        TaxPersonnel addTaxPersonnel = new TaxPersonnel();

        addTaxPersonnel.setName(newTaxPersonnel.name());
        addTaxPersonnel.setName(newTaxPersonnel.surname());
        addTaxPersonnel.setEmail(newTaxPersonnel.email());
        addTaxPersonnel.setTelephone(newTaxPersonnel.telephone());
        addTaxPersonnel.setAddress(newTaxPersonnel.address());
        addTaxPersonnel.setCityName(newTaxPersonnel.cityName());
        addTaxPersonnel.setPIva(newTaxPersonnel.pIva());
        addTaxPersonnel.setDescription(newTaxPersonnel.description());
        addTaxPersonnel.setImage("http://ui-avatars.com/api/?name="+newTaxPersonnel.name() + "+" + newTaxPersonnel.surname());
        addTaxPersonnel.setPassword(bcrypt.encode(newTaxPersonnel.password()));
        addTaxPersonnel.setCity(addCity);
        addTaxPersonnel.setRole(Role.USER);
        return taxPersonneRepository.save(addTaxPersonnel);
    }
}
