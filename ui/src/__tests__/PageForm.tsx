import { mount } from 'enzyme'
import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { PageForm } from '../components/PageForm'

describe('<PageForm />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(
              <PageForm
                inputTitle='test'
                inputPlaceholder='test'
                onInputChange={() => null}
                onSubmitClick={() => null}
              />,
            )
            .toJSON()
        expect(tree).toMatchSnapshot()
    })
    it('should fire input change handler', () => {
      const mockFn = jest.fn()
      const pageFormWrapper = mount(
        <PageForm
          inputTitle='test'
          inputPlaceholder='test'
          onInputChange={mockFn}
          onSubmitClick={() => null}
        />,
      )
      const textInput = pageFormWrapper.find('.form-field-input')
      textInput.simulate('change', {})
      expect(mockFn.mock.calls.length).toBe(1)
    })
    it('should fire submit button handler', () => {
      const mockFn = jest.fn()
      const pageFormWrapper = mount(
        <PageForm
          inputTitle='test'
          inputPlaceholder='test'
          onInputChange={() => null}
          onSubmitClick={mockFn}
        />,
      )
      const submitButton = pageFormWrapper.find('.form-field-submit')
      submitButton.simulate('click', {})
      expect(mockFn.mock.calls.length).toBe(1)
    })
})
