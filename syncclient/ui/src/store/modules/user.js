const state = {
    username: window.localStorage.getItem('token') && window.localStorage.getItem('username') ? window.localStorage.getItem('username') : '',
    token: window.localStorage.getItem('token') ? window.localStorage.getItem('token') : '',
    userType:  window.localStorage.getItem('userType') ? window.localStorage.getItem('userType') : '',
    email: window.localStorage.getItem('email') ? window.localStorage.getItem('email') : '',
}

const mutations = {
    
    CLEARTOKEN () {

        state.username = '';
        state.token = '';
        state.userType = '';
        state.email = '';
        window.localStorage.removeItem('token');
        window.localStorage.removeItem('userType');
        window.localStorage.removeItem('email');
        
        if (window.localStorage.getItem('remember') === false || window.localStorage.getItem('remember') === 'false') {
            window.localStorage.removeItem('username');
        }

    },
    SETTOKEN (state, {username, token, userType, email}) {

        state.username = username;
        state.token = token;
        state.userType = userType;
        state.email = email;
        window.localStorage.setItem('username', username);
        window.localStorage.setItem('token', token);
        window.localStorage.setItem('userType', userType);
        window.localStorage.setItem('email', email);

    },
    SETEMAIL (state, email) {
        state.email = email;
        window.localStorage.setItem('email', email);
    }
}

export default {
    state,
    mutations
}
