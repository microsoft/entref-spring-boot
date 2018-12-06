import * as React from 'react'

export interface IPageFormProps {
  inputTitle: string,
  inputPlaceholder: string,
  onInputChange: (event) => void,
  onSubmitClick: (event) => void,
}

export function PageForm(props: IPageFormProps) {
  return (
    <form className='form'>
      <label>
        <span className='form-field-pre-text'>{props.inputTitle}</span>
        <input
          className='form-field-input'
          type='text'
          name='id'
          onChange={props.onInputChange}
          placeholder={props.inputPlaceholder}
        />
        <span className='form-field-sub-text'>Leave empty for random sample</span>
      </label>
      <input
        className='form-field-submit'
        type='submit'
        value='Search'
        onClick={props.onSubmitClick}
      />
    </form>
  )
}
