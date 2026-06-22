package com.ai.springaidemo;
import java.util.Base64;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GenAiController {

private final ChatService chatService;
private final ImageService imageService;
private final RecipeService recipeService;  


public GenAiController(ChatService chatService, ImageService imageService, RecipeService recipeService){
    this.chatService=chatService;
    this.imageService = imageService;
    this.recipeService = recipeService;
    
}

    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }

@GetMapping("ask-ai-options")
    public String getResponseOptions(@RequestParam String prompt){
        return chatService.getResponseOptions(prompt);
    }
    
@GetMapping("generate-images")
public List<String> generateImages(
        @RequestParam String prompt,
        @RequestParam(defaultValue = "1") int count) {

    // Basic validation (prevents chaos)
    if (prompt == null || prompt.trim().isEmpty()) {
        throw new IllegalArgumentException("Prompt cannot be empty");
    }

    // Limit count to avoid overload (important for 8GB RAM system)
    if (count < 1) count = 1;
    if (count > 4) count = 4;

    List<byte[]> images = imageService.generateImages(prompt, count);

    
    return images.stream()
            .map(img -> Base64.getEncoder().encodeToString(img))
            .toList();
}
        


@GetMapping("recipe-creator")
public String recipeCreator(@RequestParam String ingrediants,
                                  @RequestParam(defaultValue = "any") String cuisine,
                                  @RequestParam(defaultValue = "") String dietaryRestrictions) {
    
                                    return recipeService.createRecipe(ingrediants, cuisine, dietaryRestrictions);
                                    


        
       
    }



}

    




