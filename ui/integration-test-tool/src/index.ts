import {Page} from 'puppeteer'

import {config} from '../config'

describe('Project Jackson Integration', () => {
  beforeAll(async () => {
    await page.goto(config.site)
  })

  it('should require login', async () => {
    await expect(page).toClick('.nav-link', {text: 'People'})
    await expect(page).toMatch('Please log in to access: /people')
    await expect(page).toClick('.nav-link', {text: 'Titles'})
    await expect(page).toMatch('Please log in to access: /titles')
  })

  it('should login', async () => {
    // notify us when the login page opens
    const loginPageResolver = new Promise<Page>((x) => browser.once('targetcreated', (t) => x(t.page())))

    // click log in on the main page
    await expect(page).toClick('button', {text: 'Log In'})

    // wait for login window to appear
    const loginPage = await loginPageResolver
    // wait for page to load
    await loginPage.waitForSelector('#loginHeader')
    // fill in username
    await expect(loginPage).toFill('#i0116', config.username)
    // click next
    await expect(loginPage).toClick('#idSIButton9')

    // wait for page to load
    await loginPage.waitForSelector('#displayName')
    // fill in password
    await expect(loginPage).toFill('#i0118', config.password)
    // click submit
    await expect(loginPage).toClick('#idSIButton9')

    // wait for the login window to close
    await loginPage.waitFor(1000).then(() => {
      if (!loginPage.isClosed()) {
        return loginPage.close()
      }
    })

    // wait for the main page to update
    await page.waitFor(1000)
    // expect that clicking titles  works
    await expect(page).toClick('.nav-link', {text: 'Titles'})
    // expect that we are now logged in (therefore the button says Log Out)
    await expect(page).toMatch('Log Out')
    // expect that the title page shows it's contents
    await expect(page).toMatchElement('h1', {text: 'Search Titles'})

  }, 8000)
})
