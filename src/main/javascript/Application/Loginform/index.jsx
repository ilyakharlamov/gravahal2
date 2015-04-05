/*global console*/
import React from 'react';

export default class Loginform extends React.Component {
  constructor () {
    this._submit = this._submit.bind(this);
  }

  _submit (e) {
    e.preventDefault();
    const inputLogin = this.refs.inputLogin.getDOMNode();
    const inputLoginName = inputLogin.value.trim();
    this.props.flux.getActions('gravahalapp').attemptLogin(inputLoginName);
  }

  render() {
    /*{
        if (true) {
          <div>TRUE</div>
        } else {
          <div>FALSE</div>
        }
      }*/
    return <div className="Loginform">
      <div className="Loginform_welcomemessage" >Welcome to grava hal</div>
      <form className="Loginform_form" onSubmin={this._submit}>
        <input className="Loginform_input" ref="inputLogin" type="text" placeholder="Enter your name" />
        <input className="Loginform_button" type="button" value="Login" onClick={this._submit}/>
      </form>
    </div>;
  }
}

