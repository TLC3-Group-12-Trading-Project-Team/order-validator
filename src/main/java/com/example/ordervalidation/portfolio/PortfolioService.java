package com.example.ordervalidation.portfolio;

import com.example.ordervalidation.Client.Client;
import com.example.ordervalidation.Client.ClientRepository;
import com.example.ordervalidation.Client.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    ResponseData response = new ResponseData();

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, ClientRepository clientRepository) {
        this.portfolioRepository = portfolioRepository;
        this.clientRepository = clientRepository;
    }

    // Create a portfolio
    public ResponseData addPortfolio(Portfolio portfolio){
        List<Portfolio> portfolioList = this.portfolioRepository.findAll();
        Optional<Client> client = clientRepository.findClientByEmail(portfolio.getClientEmail());
        if (client.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("No Client found");
            HttpStatus.BAD_REQUEST.value();
        }
        if (portfolio.getName().isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("Empty Portfolio Name");
            HttpStatus.BAD_REQUEST.value();
            throw new IllegalStateException("Name cannot be null");
        }
        if (portfolioList.contains(portfolio.getName())){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus("Portfolio Name Already Exist");
            HttpStatus.BAD_REQUEST.value();
            throw new IllegalStateException("Portfolio Name Already Exist");
        }
        portfolio.setClient(client.get());
        portfolio.setCreatedAt(LocalDateTime.now());
        this.portfolioRepository.save(portfolio);
        response.setCode(HttpStatus.OK.value());
        response.setStatus("Created Successfully");
        HttpStatus.OK.value();

        return response;
    }

    public List<Portfolio> getAllPortfolio(Long Id){
        return portfolioRepository.findByClientId(Id);
    }

    public ResponseData removePortfolio(Long Id){
        this.portfolioRepository.deleteById(Id);
        response.setCode(HttpStatus.OK.value());
        response.setStatus("Deleted Successfully");
        HttpStatus.OK.value();
        return response;
    }

    public Portfolio getSinglePortfolio(Long Id){
        return portfolioRepository.findById(Id).orElse(null);
    }

    public Portfolio getPortfolio(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
        return portfolio;
    }

    public Long getClientId(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
        return portfolio.getClient().getId();
    }

//    public Response updatePortfolio(Long id) {
//        this.portfolioRepository.
//    }
}

