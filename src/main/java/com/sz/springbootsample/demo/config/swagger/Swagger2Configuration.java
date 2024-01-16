package com.sz.springbootsample.demo.config.swagger;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 *
 * @author Yanghj
 * @date 1/12/2020
 */
@EnableSwagger2
@Configuration
@ConditionalOnProperty(name = "custom.swagger.enable", havingValue = "true", matchIfMissing = true)
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class Swagger2Configuration {

    @Autowired private TypeResolver typeResolver;

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // Docket, Springfox’s, primary api configuration mechanism is initialized for
                // swagger specification 2.0
                .apiInfo(apiInfo())
                // select() returns an instance of ApiSelectorBuilder to give fine grained control
                // over the endpoints exposed via swagger.
                .select()
                // .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // apis() allows selection of RequestHandler's using a predicate. The example here
                // uses an any predicate(default). Out of the box predicates provided are any, none,
                // withClassAnnotation, withMethodAnnotation and basePackage.
                .apis(RequestHandlerSelectors.any())
                // paths() allows selection of Path's using a predicate. The example here uses an
                // any predicate (default). Out of the box we provide predicates for regex, ant,
                // any, none.
                .paths(PathSelectors.any())
                .build()
                // Adds a servlet path mapping, when the servlet has a path mapping. This prefixes
                // paths with the provided path mapping.
                // .pathMapping("/")
                // Convenience rule builder that substitutes LocalDate with String when rendering
                // model properties
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                // Convenience rule builder that substitutes a generic type with one type parameter
                // with the type parameter. In this example ResponseEntity<T> with T.
                // alternateTypeRules allows custom rules that are a bit more involved. The example
                // substitutes DeferredResult<ResponseEntity<T>> with T generically.
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(
                                        DeferredResult.class,
                                        typeResolver.resolve(
                                                ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                // Flag to indicate if default http response codes need to be used or not.
                // .useDefaultResponseMessages(true)
                /*.globalResponseMessage(RequestMethod.GET, //Allows globally overriding response messages for different http methods. In this example we override the 500 error code for all GET requests …​
                Lists.newArrayList(new ResponseMessageBuilder()
                        .code(500)
                        .message("Internal Server Error")
                        .responseModel(new ModelRef("Error"))   //…​ and indicate that it will use the response model Error (which will be defined elsewhere)
                        .build()))*/
                // Sets up the security schemes used to protect the apis. Supported schemes are
                // ApiKey, BasicAuth and OAuth
                .securitySchemes(Lists.newArrayList(apiKey()))
                // Provides a way to globally set up security contexts for operation. The idea here
                // is that we provide a way to select operations to be protected by one of the
                // specified security schemes.
                .securityContexts(Lists.newArrayList(securityContext()))
        // Allows globally configuration of default path-/request-/headerparameters which are common
        // for every rest operation of the api, but aren`t needed in spring controller method
        // signature (for example authenticaton information). Parameters added here will be part of
        // every API Operation in the generated swagger specification. on how the security is setup
        // the name of the header used may need to be different. Overriding this value is a way to
        // override the default behavior.
        /*.globalOperationParameters(
        Lists.newArrayList(new ParameterBuilder()
                .name("someGlobalParameter")
                .description("Description of someGlobalParameter")
                .modelRef(new ModelRef("string"))
                .parameterType("query")
                .required(true)
                .build()))*/
        ;
    }

    @Bean
    UiConfiguration uiConfig() {
        // swagger-ui ui configuration currently only supports the validation url
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

    @Value("${spring.application.name:application}")
    private String applicationName;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documents")
                .description("Swagger API Documents for " + applicationName)
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        // Here we use ApiKey as the security schema that is identified by the name:Authorization
        return new ApiKey("token", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                // Selector for the paths this security context applies to. 默认^.*$匹配所有URL
                .forPaths(PathSelectors.regex("^.*$"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        // Here we use the same key defined in the security scheme:Authorization
        return Lists.newArrayList(new SecurityReference("token", authorizationScopes));
    }
}
