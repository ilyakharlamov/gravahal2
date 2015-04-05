import React from 'react';

require('./style.sass');

export default class NewCityForm extends React.Component {
  constructor () {
    this._submit = this._submit.bind(this);
  }

  componentDidMount() {
    this.refs.input.getDOMNode().focus();
  }

  _submit (e) {
    e.preventDefault();
    const input = this.refs.input.getDOMNode();
    const select = this.refs.select.getDOMNode();
    const intervalsec = parseInt(select.value);
    const cityquery = input.value.trim();
    this.props.flux.getActions('weatherapp').createCitytileAttempt(cityquery, intervalsec);
    input.value = '';
  }

  render () {
    return <div className="NewCity">
      <div className="NewCityForm__errormessage" >{this.props.errormessage}</div>
      <form className="NewCityForm" onSubmit={this._submit}>
        <input className="NewCityForm__input" ref="input" type="text" placeholder="Enter City" />
        <select ref="select">
          <option value="5">5 seconds</option>
          <option value="10">10 seconds</option>
          <option value="20">20 seconds</option>
        </select>
        <input type="button" value="Add" onClick={this._submit}/>
      </form>
    </div>;
  }
}
