import fetch from 'node-fetch'
import { Page } from 'puppeteer'

import { config } from '../config'

/**
 * Provide a quick log about what jest is configured to point to
 */
// tslint:disable-next-line:no-console
process.stdout.write(`Project Jackson Integration Tests
  Frontend Base URL: ${config.ui_site}
  Backend Base URL: ${config.backend_site}
  Username: ${config.username}
  Password: ${'*'.repeat(config.password.length)}
  \n`)

describe('Integration Tests', () => {
  beforeAll(async () => {
    await page.goto(config.ui_site)
  })

  it('health endpoint should be active', async () => {
    const res = await fetch(`${config.backend_site}/health`, {
      method: 'GET',
    })

    const status = res.status
    const data = await res.text()

    expect(status).toBe(200)
    expect(data).toBe('Alive')
  })

  it('ui should require login', async () => {
    await expect(page).toClick('.nav-link', {text: 'People'})
    await expect(page).toMatch('Please log in to access: /people')
    await expect(page).toClick('.nav-link', {text: 'Titles'})
    await expect(page).toMatch('Please log in to access: /titles')
  })

  it('ui should login', async () => {
    // notify us when the login page opens
    const loginPageResolver = new Promise<Page>((x) => browser.once('targetcreated', (t) => x(t.page())))

    // click log in on the main page
    await expect(page).toClick('button', {text: 'Log In'})

    // wait for login window to appear
    const loginPage = await loginPageResolver
    // wait for page to load
    await loginPage.waitForSelector('#loginHeader')
    // fill in username
    await expect(loginPage).toFill('input[type="email"]', config.username)
    // click next
    await expect(loginPage).toClick('input[type="submit"]')

    // wait for page to load
    await loginPage.waitForSelector('#displayName')
    // fill in password
    await expect(loginPage).toFill('input[type="password"]', config.password)
    // click submit
    await expect(loginPage).toClick('input[type="submit"]')

    // wait for the login window to close
    await loginPage.waitFor(1000).then(() => {
      if (!loginPage.isClosed()) {
        return loginPage.close()
      }
    })

    // wait for the main page to update
    await page.waitFor(1000)
    // expect that clicking titles works
    await expect(page).toClick('.nav-link', {text: 'Titles'})
    // expect that we are now logged in (therefore the button says Log Out)
    await expect(page).toMatch('Log Out')
    // expect that the title page shows its contents
    await expect(page).toMatchElement('h1', {text: 'Search Titles'})

  }, 8000)

  describe('Authenticated', () => {
    /**
     * The AAD id token
     */
    let idToken: string

    beforeAll(async () => {
      // save the id token from the above login flow - these next few will need it
      idToken = await page.evaluate(() => sessionStorage.getItem('msal.idtoken')) as string
    })

    it('should query titles', async () => {
      // expect that clicking titles works
      await expect(page).toClick('.nav-link', {text: 'Titles'})
      await expect(page).toMatchElement('h1', {text: 'Search Titles'})
      // expect that we are logged in (therefore the button says Log Out)
      await expect(page).toMatch('Log Out')
      // click search
      await expect(page).toClick('input[type="submit"][value="Search"]')

      // wait for the response
      await page.waitForResponse((req) => {
        return req.url().includes('/titles')
      })
      await page.waitFor(500)

      // get the results view
      const resultsPre = await page.$('.results-view')
      const resultsText = await page.evaluate((pre) => pre.textContent, resultsPre) as string
      await resultsPre.dispose()

      // parse it
      const resultsObj = JSON.parse(resultsText)
      // expect it to contain no errors
      expect(resultsObj.error).toBeUndefined()
      // and some data (check the primary id field length)
      expect(resultsObj.tconst.length).toBeGreaterThan(0)
    })

    it('should query people', async () => {
      // expect that clicking people works
      await expect(page).toClick('.nav-link', {text: 'People'})
      await expect(page).toMatchElement('h1', {text: 'Search People'})
      // expect that we are logged in (therefore the button says Log Out)
      await expect(page).toMatch('Log Out')
      // click search
      await expect(page).toClick('input[type="submit"][value="Search"]')

      // wait for the response
      await page.waitForResponse((req) => {
        return req.url().includes('/people')
      })
      await page.waitFor(500)

      // get the results view
      const resultsPre = await page.$('.results-view')
      const resultsText = await page.evaluate((pre) => pre.textContent, resultsPre) as string
      await resultsPre.dispose()

      // parse it
      const resultsObj = JSON.parse(resultsText)
      // expect it to contain no errors
      expect(resultsObj.error).toBeUndefined()
      // and some data (check the primary id field length)
      expect(resultsObj.nconst.length).toBeGreaterThan(0)
    })

    it('GET /titles', async () => {
      const res = await fetch(`${config.backend_site}/titles`, {
        headers: {
          Authorization: `Bearer ${idToken}`,
        },
        method: 'GET',
      })

      const status = res.status
      const data = await res.json()

      expect(status).toBe(200)
      expect(data._embedded.titles.length).toBeGreaterThan(0)
    })

    describe('Backend titles', () => {
      /**
       * The first title in the titles
       * Note: We populate this in the first test below, and reference it for the others
       */
      let firstTitle: any

      beforeAll(async () => {
        const res = await fetch(`${config.backend_site}/titles`, {
          headers: {
            Authorization: `Bearer ${idToken}`,
          },
          method: 'GET',
        })

        const data = await res.json()

        firstTitle = data._embedded.titles[0]
      })

      it('GET /title', async () => {
        const res = await fetch(`${config.backend_site}/titles/${firstTitle.tconst}`, {
          headers: {
            Authorization: `Bearer ${idToken}`,
          },
          method: 'GET',
        })

        const status = res.status
        const data = await res.json()

        expect(status).toBe(200)
        expect(data).toEqual(firstTitle)
      })

      // tslint:disable:object-literal-sort-keys
      const obj = {
        tconst: 'inttest01',
        titleType: 'short',
        primaryTitle: 'integration test 01',
        originalTitle: 'integration test 01',
        isAdult: false,
        startYear: 1337,
        endYear: null,
        runtimeMinutes: 1337,
        genres: [
          'test',
          'integration',
        ],
      }
      // tslint:enable:object-literal-sort-keys

      describe('POST /title', () => {
        beforeAll(async () => {
          await fetch(`${config.backend_site}/titles/${obj.tconst}`, {
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'DELETE',
          })
        })

        it('creates', async () => {
          const res = await fetch(`${config.backend_site}/titles`, {
            body: JSON.stringify(obj),
            headers: {
              'Accept': 'application/json',
              'Authorization': `Bearer ${idToken}`,
              'Content-Type': 'application/json',
            },
            method: 'POST',
          })

          const status = res.status
          const data = await res.json()

          expect(status).toBe(201)
          // we can't do an object compare because the link fields will be in the response
          // instead we ensure that each kv pair from the original object is in the created object
          Object.keys(obj).forEach((k) => {
            expect(data[k]).toEqual(obj[k])
          })
        })
      })

      describe('DELETE /title', () => {
        beforeAll(async () => {
          const res = await fetch(`${config.backend_site}/titles`, {
            body: JSON.stringify(obj),
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'POST',
          })
        })

        // note: if this test fails, you may wish to go manually delete the inttest01 object
        it('deletes', async () => {
          const res = await fetch(`${config.backend_site}/titles/${obj.tconst}`, {
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'DELETE',
          })

          const status = res.status
          const data = await res.text()

          expect(status).toBe(204)
          expect(data).toEqual('')
        })
      })
    })

    it('GET /people', async () => {
      const res = await fetch(`${config.backend_site}/people`, {
        headers: {
          Authorization: `Bearer ${idToken}`,
        },
        method: 'GET',
      })

      const status = res.status
      const data = await res.json()

      expect(status).toBe(200)
      expect(data._embedded.persons.length).toBeGreaterThan(0)
    })

    describe('Backend people', () => {
      /**
       * The first person in the people
       * Note: We populate this in the first test below, and reference it for the others
       */
      let firstPerson: any

      beforeAll(async () => {
        const res = await fetch(`${config.backend_site}/people`, {
          headers: {
            Authorization: `Bearer ${idToken}`,
          },
          method: 'GET',
        })

        const data = await res.json()

        firstPerson = data._embedded.persons[0]
      })

      it('GET /people', async () => {
        const res = await fetch(`${config.backend_site}/people/${firstPerson.nconst}`, {
          headers: {
            Authorization: `Bearer ${idToken}`,
          },
          method: 'GET',
        })

        const status = res.status
        const data = await res.json()

        expect(status).toBe(200)
        expect(data).toEqual(firstPerson)
      })
    
      // tslint:disable:object-literal-sort-keys
      const obj = {
        nconst: 'inttest01',
        primaryName: 'integration user 01',
        birthYear: 1337,
        deathYear: 1337,
        primaryProfession: [
          'dancer',
        ],
        knownForTitles: [],
      }
      // tslint:enable:object-literal-sort-keys

      describe('POST /people', () => {

        beforeAll(async () => {
          await fetch(`${config.backend_site}/people/${obj.nconst}`, {
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'DELETE',
          })
        })

        it('creates', async () => {
          const res = await fetch(`${config.backend_site}/people`, {
            body: JSON.stringify(obj),
            headers: {
              'Accept': 'application/json',
              'Authorization': `Bearer ${idToken}`,
              'Content-Type': 'application/json',
            },
            method: 'POST',
          })

          const status = res.status
          const data = await res.json()

          expect(status).toBe(201)
          // we can't do an object compare because the link fields will be in the response
          // instead we ensure that each kv pair from the original object is in the created object
          Object.keys(obj).forEach((k) => {
            expect(data[k]).toEqual(obj[k])
          })
        })
      })

      describe('DELETE /people', () => {
        beforeAll(async () => {
          const res = await fetch(`${config.backend_site}/people`, {
            body: JSON.stringify(obj),
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'POST',
          })
        })

        // note: if this test fails, you may wish to go manually delete the inttest01 object
        it('deletes', async () => {
          const res = await fetch(`${config.backend_site}/people/${obj.nconst}`, {
            headers: {
              Authorization: `Bearer ${idToken}`,
            },
            method: 'DELETE',
          })

          const status = res.status
          const data = await res.text()

          expect(status).toBe(204)
          expect(data).toEqual('')
        })
      })
    })
  })
})
