public Authentication authenticate(Authentication authentication) throws AuthenticationException {// Determine username
    String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    boolean cacheWasUsed = true;
    UserDetails user = this.userCache.getUserFromCache(username);
    if (user == null) {
        cacheWasUsed = false;
        try {　　　　　　　　　 // 子类根据自身情况从指定的地方加载认证需要的用户信息
            user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
        }

        // ...
        try {　　　　　　　// 前置检查，一般是检查账号状态，如是否锁定之类
        preAuthenticationChecks.check(user);　　　　　　　// 进行一般逻辑认证，如 DaoAuthenticationProvider 实现中的密码验证就是在这里完成的
        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        }
    }
    // ...
　　　　 //　后置检查，如可以检查密码是否过期之类
    postAuthenticationChecks.check(user);

　　　//　 ...
　　　// 验证成功之后返回包含完整认证信息的 Authentication 对象
    return createSuccessAuthentication(principalToReturn, authentication, user);
}