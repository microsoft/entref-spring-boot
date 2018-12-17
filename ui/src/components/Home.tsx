import * as React from 'react'

export interface IHomeProps { path?: string, }

export interface IHomeState { image: string }

export class Home extends React.Component<IHomeProps, IHomeState> {
    public render() {
        return(
        <div className = 'page-container--home' >
            <h1 className='home-title'>Welcome to Project Jackson!</h1>
            <p className='center'>
                <b>Project Jackson</b> is an open-source project, created by members of Microsoft's CSE team,
                 to demonstrate the handiness and effictiveness of Azure resources.
                <br />
                <br />

                Azure resources used include <a
                    href='https://docs.microsoft.com/en-us/azure/cosmos-db/'>CosmosDB
                    </a>, <a href='https://azure.microsoft.com/en-us/services/traffic-manager/'>Traffic Manager</a>, <a
                    href='https://azure.microsoft.com/en-us/services/app-service/containers/'>
                    App Service for Containers
                </a>,
                and <a href='https://azure.microsoft.com/en-us/services/application-gateway/'>Application Gateway</a>.
                <br />
                <br />

                In order to demonstrate Cosmos DB performance with large amounts of data,
                the project imports historical movie data from IMDb.
                See <a href='https://datasets.imdbws.com/'> here for downloadable IMDB datasets</a>.
                The datasets include 8.9 million people, 5.3 million movies and 30 million relationships between them.
                <br />
                <br />

                Languages used for this project include Java, Javascript, and Typescript.
                These languages were selected because they are all well documented and well-suited for our purposes.
                <br />
                <br />
                Technologies used include:
            </p>
            <ul className='center'>
                <li><i>Java Spring</i>, a platform that provides infrastructure support for Java applications </li>
                <li><i>Docker</i>, a tool used in order to isolate different microservices,
                    allowing for easy maintenance and testing</li>
                <li><i>React</i>, a component-based front-end Javascript library,
                    used for building UIs </li>
                <li><i>Reach Router</i>, a Javascript library that manages the focus of apps on
                    route transitions and focuses on user accessibility</li>
                <li><i>Jest</i>, a Javascript library used to test front-end rendering</li>
                <li><i>Webpack</i>, a bundler for Javascript files</li>
                <li><i>Custom Azure Resource Manager (ARM) templates</i> were utilized,
                    to allow one-click deployment of Azure resources and services</li>
            </ul>
        </div>
        )
    }
}
