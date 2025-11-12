import {Navigate, Outlet} from "react-router-dom";
import type {Users} from "@/types/Users.ts";

type ProtectedRouteProps = {
    user: Users | undefined | null;
}

export default function ProtectedRoute(props: Readonly<ProtectedRouteProps>) {

    if (props.user === undefined) {
        return (<h3>Loading...</h3>);
    }

    return (
        props.user ? <Outlet/> : <Navigate to={"/login"} replace/>
    )
}