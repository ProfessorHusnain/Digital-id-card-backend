package eEarn.com.userAuth.Services;

import eEarn.com.userAuth.Dto.*;
import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.BusinessHolder;
import eEarn.com.userAuth.Entity.GlobalCard;
import eEarn.com.userAuth.Entity.Tokens;
import eEarn.com.userAuth.Exceptions.NotFoundException;
import eEarn.com.userAuth.Exceptions.UnAuthorizedException;
import eEarn.com.userAuth.Repository.GlobalCardRepository;
import eEarn.com.userAuth.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GlobalCardService {
    private final QRCodeService qrCodeService;
    private final GlobalCardRepository repository;

    private final ApplicationUserService userService;

    // private final JwtTokenVerifier tokenVerifier;
    private final TokenService tokenService;
    private final ProductRepository productRepository;

    public String getGlobalCard(Long user) {
        GlobalCard globalCard = GlobalCard.builder()
                .globalCardId(UUID.randomUUID().toString())
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .ownerId(user)
                .build();
        byte[] cardInByte = qrCodeService.createQRCode(globalCard.getGlobalCardId(), 250, 250);
        String GlobalCard = Base64.getEncoder().encodeToString(cardInByte);
        repository.save(globalCard);
        return GlobalCard;
    }

    public String getGlobalCardWIthGlobalID(String GlobalCardId) {

        byte[] cardInByte = qrCodeService.createQRCode(GlobalCardId, 250, 250);
        String GlobalCard = Base64.getEncoder().encodeToString(cardInByte);
        return GlobalCard;
    }

    @Transactional
    public String updateGlobalCard(BusinessUserDto businessUserDto) {

        //Getting user from token store
        Optional<Tokens> token = tokenService.getToken(businessUserDto.getConsumerID());
        // Get the use want to cunsume the card
        System.out.println(token.get().getUsername());
        ApplicationUser userConsume =
                userService.checkAndGetApplicationUserByUsernameOrThrow(token.get().getUsername());

        Optional<GlobalCard> byGlobalCardId =
                repository.
                        findByGlobalCardId(businessUserDto.getCardOwnerID());
        if (!byGlobalCardId.isPresent()) {
           throw  new NotFoundException("You are meet with fraudia");
        }
        else if (!byGlobalCardId.get().getIsActive()) {
            throw  new UnAuthorizedException("Card has been expired");
        }

        GlobalCard globalCard = byGlobalCardId.get();

        //Setting up product if exsit
        if (!businessUserDto.getProductName().isEmpty()&&
                !businessUserDto.getProductPrice().isEmpty()){

            BusinessHolder businessHolder= BusinessHolder.builder()
                    .productName(businessUserDto.getProductName())
                    .productPrice(businessUserDto.getProductPrice())
                    .build();

            productRepository.save(businessHolder);
            globalCard.setProductId(businessHolder.getId());
        }

        globalCard.setCardType(businessUserDto.getConsumeType());
        globalCard.setExpireOn(LocalDateTime.now());
        globalCard.setVisitorId(userConsume.getId());
        globalCard.setIsActive(false);
        //globalCard.setProductId();

        repository.save(globalCard);
        return "User Has been identify";
    }
    public ApplicationUserDTO getApplicationUserProfile(String token) {
        Optional<Tokens> token1 = tokenService.getToken(token);
        ApplicationUser user = userService.checkAndGetApplicationUserByUsernameOrThrow(token1.get().getUsername());
        return ApplicationUserDTO
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .AccountType(user.getUserRole().name())
                .build();
    }
    public UserProfileResponseDTO getProfile(String tokenFromTokenStore) {
        Optional<Tokens> token = tokenService.getToken(tokenFromTokenStore);

        ApplicationUser user = userService
                .checkAndGetApplicationUserByUsernameOrThrow(token.get().getUsername());
        String card = getCard(user);

        List<GlobalCard> byOwnerId = repository.findByOwnerId(user.getId());

               List<GlobalCardDTO> usedGlobalCardList = byOwnerId.stream()
                .filter(m -> m.getIsActive() == false)
                .map(m -> GlobalCardDTO.builder()
                        .cardConsumer(getConsumerCardName(m.getVisitorId()))
                        .consumeOn(m.getExpireOn())
                        .createOn(m.getCreatedOn())
                        .consumeType(m.getCardType())
                        .build()).collect(Collectors.toList());

        UserProfileResponseDTO userProfileResponseDTO=
                UserProfileResponseDTO
                        .builder()
                        .username(user.getFirstName()+" "+user.getLastName())
                        .card(card)
                        .globalCardDTOList(usedGlobalCardList)
                        .build();

        return userProfileResponseDTO;

    }

    private String getConsumerCardName(Long visitorId) {
        ApplicationUser applicationUserByIdOrThrow = userService.getApplicationUserByIdOrThrow(visitorId);

        return applicationUserByIdOrThrow.getFirstName()+" "+applicationUserByIdOrThrow.getLastName();
    }

    private String getCard(ApplicationUser user) {
        List<GlobalCard> byOwnerId = repository.findByOwnerId(user.getId());
        String card;
        if (!byOwnerId.isEmpty()) {
            List<GlobalCard> collect = byOwnerId.stream()
                    .filter(m -> m.getIsActive() == true).collect(Collectors.toList());
            System.out.println(collect);
            if (!collect.isEmpty()) {
                GlobalCard globalCard = collect.stream().findFirst().get();
                globalCard.getGlobalCardId();
                card = getGlobalCardWIthGlobalID(globalCard.getGlobalCardId());
            }else {
                card = getGlobalCard(user.getId());
            }
        } else {
            // in Qr Code
            card = getGlobalCard(user.getId());
        }
        return card;
    }

    public List<BusinessResponseDTO> getBusinessDetails(String id) {
        Optional<Tokens> token = tokenService.getToken(id);
        ApplicationUser user = userService.checkAndGetApplicationUserByUsernameOrThrow(token.get().getUsername());
        List<GlobalCard> byOwnerId = repository.findByVisitorId(user.getId());
        List<BusinessResponseDTO> collect = byOwnerId.stream()
                .filter(m -> m.getProductId() != null).map(m ->
                        BusinessResponseDTO
                                .builder()
                                .purchaserName(getPurchaserName(m.getOwnerId()))
                                .saleTime(m.getExpireOn().toString())
                                .productName(getProductName(m.getProductId()))
                                .productPrice(getProductPrice(m.getProductId()))
                                .build()).collect(Collectors.toList());
        System.out.println(collect);
        return collect;

    }

    private String getProductPrice(Long productId) {
        String productPrice = productRepository.findById(productId).get().getProductPrice();
        System.out.println(productId+" "+ productPrice);
        return productPrice;
    }

    private String getProductName(Long productId) {

        return productRepository.findById(productId).get().getProductName();
    }

    private String getPurchaserName(Long ownerId) {
        ApplicationUser applicationUserByIdOrThrow = userService.getApplicationUserByIdOrThrow(ownerId);
        return applicationUserByIdOrThrow.getFirstName()+" "+applicationUserByIdOrThrow.getLastName();
    }
  /*  public String getUsernameFromToken(String token){
        String request = tokenVerifier.getBearerFreeTokenFromRequest(token);
        Claims tokenBody = tokenVerifier.getTokenBody(request);
        Optional<Tokens> token1 = tokenVerifier.getToken(tokenBody);
        return token1.get().getUsername();
    }*/
}
