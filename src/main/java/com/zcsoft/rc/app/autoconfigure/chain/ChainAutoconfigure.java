package com.zcsoft.rc.app.autoconfigure.chain;


import com.sharingif.cube.core.chain.ChainImpl;
import com.sharingif.cube.core.chain.command.Command;
import com.sharingif.cube.core.handler.chain.*;
import com.sharingif.cube.security.handler.chain.command.authentication.SecurityAuthenticationCommand;
import com.sharingif.cube.security.web.handler.chain.command.authentication.SessionConcurrentWebCommand;
import com.sharingif.cube.security.web.handler.chain.command.user.CoreUserHttpSessionManageWebCommand;
import com.sharingif.cube.security.web.handler.chain.command.user.InvalidateHttpSessionWebCommand;
import com.sharingif.cube.security.web.handler.chain.session.SessionExpireChain;
import com.sharingif.cube.security.web.spring.handler.chain.command.session.SessionRegistryCommand;
import com.zcsoft.rc.app.chain.UserAuthorityAccessDecisionChain;
import com.zcsoft.rc.app.chain.command.UserConvertToUserLoginReqCommand;
import com.zcsoft.rc.app.chain.command.UserConvertToUserTokenLoginReqCommand;
import com.zcsoft.rc.app.chain.command.UserLoginReqConvertToUserCommand;
import com.zcsoft.rc.app.chain.command.UserTokenLoginReqConvertToUserCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ChainAutoconfigure {

    @Bean("loginChain")
    public ChainImpl<HandlerMethodContent> createLoginPassChain(
            UserLoginReqConvertToUserCommand userLoginReqConvertToUserCommand
            , SecurityAuthenticationCommand passwordSecurityAuthenticationCommand
            , SessionConcurrentWebCommand sessionConcurrentWebCommand
            , InvalidateHttpSessionWebCommand invalidateHttpSessionWebCommand
            , CoreUserHttpSessionManageWebCommand coreUserHttpSessionManageWebCommand
            , SessionRegistryCommand sessionRegistryCommand
            , UserConvertToUserLoginReqCommand userConvertToUserLoginReqCommand
    ) {
        List<Command<? super HandlerMethodContent>> commands = new ArrayList<Command<? super HandlerMethodContent>>();
        commands.add(userLoginReqConvertToUserCommand);
        commands.add(passwordSecurityAuthenticationCommand);
        commands.add(sessionConcurrentWebCommand);
        commands.add(invalidateHttpSessionWebCommand);
        commands.add(coreUserHttpSessionManageWebCommand);
        commands.add(sessionRegistryCommand);
        commands.add(userConvertToUserLoginReqCommand);

        ChainImpl loginChain = new ChainImpl();
        loginChain.setCommands(commands);

        return loginChain;
    }

    @Bean("tokenLoginChain")
    public ChainImpl<HandlerMethodContent> createTokenLoginChain(
            UserTokenLoginReqConvertToUserCommand userTokenLoginReqConvertToUserCommand
            , SecurityAuthenticationCommand tokenSecurityAuthenticationCommand
            , SessionConcurrentWebCommand sessionConcurrentWebCommand
            , InvalidateHttpSessionWebCommand invalidateHttpSessionWebCommand
            , CoreUserHttpSessionManageWebCommand coreUserHttpSessionManageWebCommand
            , SessionRegistryCommand sessionRegistryCommand
            , UserConvertToUserTokenLoginReqCommand userConvertToUserTokenLoginReqCommand
    ) {
        List<Command<? super HandlerMethodContent>> commands = new ArrayList<Command<? super HandlerMethodContent>>();
        commands.add(userTokenLoginReqConvertToUserCommand);
        commands.add(tokenSecurityAuthenticationCommand);
        commands.add(sessionConcurrentWebCommand);
        commands.add(invalidateHttpSessionWebCommand);
        commands.add(coreUserHttpSessionManageWebCommand);
        commands.add(sessionRegistryCommand);
        commands.add(userConvertToUserTokenLoginReqCommand);

        ChainImpl loginChain = new ChainImpl();
        loginChain.setCommands(commands);

        return loginChain;
    }

    @Bean("noUserChain")
    public UserAuthorityAccessDecisionChain createNoUserChain() {

        List<String> excludeMethods = new ArrayList<String>();
        excludeMethods.add("user.controller.UserController.login");
        excludeMethods.add("user.controller.UserController.tokenLogin");


        UserAuthorityAccessDecisionChain noUserChain = new UserAuthorityAccessDecisionChain();
        noUserChain.setExcludeMethods(excludeMethods);
        noUserChain.setReplaceContent("com.zcsoft.rc.");

        return noUserChain;
    }

    @Bean(name="springMCVChains")
    public MultiHandlerMethodChain createSpringMCVChains(
            MonitorPerformanceChain controllerMonitorPerformanceChain
            , SessionExpireChain sessionExpireChain
            , UserAuthorityAccessDecisionChain userAuthorityAccessDecisionChain
            , AnnotationHandlerMethodChain annotationHandlerMethodChain
    ) {

        List<HandlerMethodChain> chains = new ArrayList<HandlerMethodChain>(3);
        chains.add(controllerMonitorPerformanceChain);
        chains.add(sessionExpireChain);
        chains.add(userAuthorityAccessDecisionChain);
        chains.add(annotationHandlerMethodChain);

        MultiHandlerMethodChain springMVCHandlerMethodContent = new MultiHandlerMethodChain();
        springMVCHandlerMethodContent.setChains(chains);

        return  springMVCHandlerMethodContent;
    }

}
