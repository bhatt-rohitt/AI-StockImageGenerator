package com.ai.springaidemo;

import java.util.Map;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final ChatModel chatModel;

    public RecipeService(ChatModel chatModel){
        this.chatModel = chatModel;

        
    }

    public String createRecipe(String ingrediants,
         String cuisine, 
         String dietaryRestrictions){

            var template = """
                I want to create a Recipe using the following ingrediants: {ingrediants}.
                The cuisine type i prefer is {cuisine}.
                Please consider the following dietay restrictions: {dietaryRestrictions}.
                Please provide me with a detailed recipe including title, list of ingrediants, and cooking instructions
                    """;
                
                    PromptTemplate promptTemplate =  new PromptTemplate(template);
                    Map<String, Object> params = Map.of(
                        "ingrediants" ,ingrediants,
                        "cuisine", cuisine,
                        "dietaryRestrictions", dietaryRestrictions
                    );
           
                    Prompt prompt = promptTemplate.create(params);
                 return  chatModel.call(prompt).getResult().getOutput().getText();
            

        
    }
    

}
